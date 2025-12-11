package com.babymonitor.network.interceptor

import com.ecreditpal.cashloan.indo.BuildConfig
import com.example.sakubersama.common.utils.TokenUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * 1111111111，11heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("x-version", BuildConfig.VERSION_NAME)
            .header("x-package-name", BuildConfig.APPLICATION_ID)
        try {
            if (TokenUtils.getToken().isNotEmpty()) {
                builder.header(
                    "x-auth-token", TokenUtils.getToken()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return chain.proceed(builder.build())
    }

}