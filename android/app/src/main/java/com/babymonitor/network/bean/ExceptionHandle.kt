package com.example.sakubersama.common.network.bean

import android.net.ParseException
import android.net.http.HttpException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import java.net.ConnectException

/**
 * 11　: hegaojian
 * 11　: 2019/12/17
 * 11　: 1111111111111111
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        e?.printStackTrace()
        val ex: AppException
        e?.let {
            when (it) {
                is retrofit2.HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR,e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN,e)
        return ex
    }
}