package com.babymonitor.network.interceptor

import com.example.sakubersama.common.BaseApplication
import com.example.sakubersama.common.KeyConstant
import com.example.sakubersama.common.network.bean.ApiResponse
import com.example.sakubersama.common.network.bean.AppException
import com.example.sakubersama.common.utils.DeviceTools
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.jvm.Throws

/**
 */
class ResponseInterceptor : Interceptor {
    val gson: Gson by lazy { Gson() }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse: Response
        try {
            originalResponse = chain.proceed(request)
        } catch (e: IOException) {
            updateCrashData(request, -1, e.message ?: "")
            throw e
        }
        //pdf 11
        if (request.url().encodedPath().endsWith(".pdf")) {
            return originalResponse
        }

        return if (originalResponse.body() != null && originalResponse.body()!!
                .contentType() != null
        ) {
            val mediaType = originalResponse.body()!!.contentType()
            val string = originalResponse.body()!!.string()
            val responseBody = ResponseBody.create(mediaType, string)

            try {
                val apiResponse = gson.fromJson(string, ApiResponse::class.java)
                if (apiResponse.code == KeyConstant.ERROR_CODE_LOGIN) {
                    BaseApplication.mAppViewModel.clearDataToLogin()
                }

                if (!apiResponse.isSucces()) {
                    updateCrashData(request, originalResponse.code(), originalResponse.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            originalResponse.newBuilder().body(responseBody).build()
        } else {
            updateCrashData(request, originalResponse.code(), originalResponse.message())
            originalResponse
        }


    }

    private fun updateCrashData(mRequest: Request, code: Int, message: String) {
        if (code == 0) {
            return
        }
        if (code == 200) {
            return
        }
        val url = mRequest.url().toString()
        if (!url.contains(KeyConstant.debug_URL) && !url.contains(KeyConstant.reportDomain_URL)) {
            BaseApplication.mAppViewModel.setCrashData(
                KeyConstant.NET + DeviceTools.getDeviceNo(),
                Thread.currentThread(),
                AppException(errCode = code, error = "$code----$url----$message")
            )
        }
    }

}
