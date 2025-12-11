package com.example.sakubersama.common.network.interceptor.logging

import okhttp3.MediaType
import okhttp3.Request

/**
 * 11　: hegaojian
 * 11　: 2020/3/26
 * 11　:
 */
interface FormatPrinter {
    /**
     * 11111111, 111111 {[okhttp3.RequestBody]} 1111111
     *
     * @param request
     * @param bodyString 11111111111111(111)
     */
    fun printJsonRequest(request: Request, bodyString: String)

    /**
     * 11111111, 111111 {[okhttp3.RequestBody]} 1 `null` 11111111
     *
     * @param request
     */
    fun printFileRequest(request: Request)

    /**
     * 11111111, 111111 {[okhttp3.ResponseBody]} 1111111
     *
     * @param chainMs      1111111(1111)
     * @param isSuccessful 111111
     * @param code         111
     * @param headers      111
     * @param contentType  111111111111
     * @param bodyString   11111111(111)
     * @param segments     111111111
     * @param message      1111
     * @param responseUrl  1111
     */
    fun printJsonResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        contentType: MediaType?,
        bodyString: String?,
        segments: List<String?>,
        message: String,
        responseUrl: String
    )

    /**
     * 11111111, 111111 {[okhttp3.ResponseBody]} 1 `null` 11111111
     *
     * @param chainMs      1111111(1111)
     * @param isSuccessful 111111
     * @param code         111
     * @param headers      111
     * @param segments     111111111
     * @param message      1111
     * @param responseUrl  1111
     */
    fun printFileResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        segments: List<String?>,
        message: String,
        responseUrl: String
    )
}