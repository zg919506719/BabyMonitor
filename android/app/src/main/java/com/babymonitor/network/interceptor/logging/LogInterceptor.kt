package com.example.sakubersama.common.network.interceptor.logging

import android.util.Log
import com.example.sakubersama.common.utils.CharacterHandler.Companion.jsonFormat
import com.example.sakubersama.common.utils.UrlEncoderUtils.Companion.hasUrlEncoded
import com.example.sakubersama.common.utils.ZipHelper.Companion.decompressForGzip
import com.example.sakubersama.common.utils.ZipHelper.Companion.decompressToStringForZlib
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import java.util.Locale
import java.util.Locale.getDefault
import java.util.concurrent.TimeUnit

class LogInterceptor : Interceptor {
    private val mPrinter: FormatPrinter = DefaultFormatPrinter()
    private val printLevel = Level.ALL

    constructor() {}

    constructor(printLevel: Level?) {

    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.REQUEST
        if (logRequest) {
            //111111
            if (request.body() != null && isParseable(
                    request.body()!!.contentType()
                )
            ) {
                mPrinter.printJsonRequest(request, parseParams(request))
            } else {
                mPrinter.printFileRequest(request)
            }
        }
        val logResponse =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response
        originalResponse = try {
            chain.proceed(request)
        } catch (e: Exception) {
            e.message?.let {
                Log.d("Http Error: %s", it)
            }
            throw e
        }
        val t2 = if (logResponse) System.nanoTime() else 0
        val responseBody = originalResponse.body()

        //111111
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse)
        }
        if (logResponse) {
            val segmentList =
                request.url().encodedPathSegments()
            val header: String = if (originalResponse.networkResponse() == null) {
                originalResponse.headers().toString()
            } else {
                originalResponse.networkResponse()!!.request().headers().toString()
            }
            val code = originalResponse.code()
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message()
            val url = originalResponse.request().url().toString()
            if (responseBody != null && isParseable(responseBody.contentType())) {
                mPrinter.printJsonResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                    code, header, responseBody.contentType(), bodyString, segmentList, message, url
                )
            } else {
                mPrinter.printFileResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                    isSuccessful, code, header, segmentList, message, url
                )
            }
        }
        return originalResponse
    }

    /**
     * 111111
     *
     * @param request     [Request]
     * @param response    [Response]
     * @param logResponse 11111111
     * @return 11111111
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun printResult(
        request: Request,
        response: Response,
        logResponse: Boolean
    ): String? {
        return try {
            //1111111111
            val responseBody = response.newBuilder().build().body()
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            //11content11111
            val encoding = response
                .headers()["Content-Encoding"]
            val clone = buffer.clone()

            //11response content
            parseContent(responseBody, encoding, clone)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
        }
    }

    /**
     * 1111111111
     *
     * @param responseBody [ResponseBody]
     * @param encoding     1111
     * @param clone        11111111111
     * @return 11111111
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer
    ): String? {
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        //content 11 gzip 11
        return if ("gzip".equals(encoding, ignoreCase = true)) {
            //11
            decompressForGzip(
                clone.readByteArray(),
                convertCharset(charset)
            )
        } else if ("zlib".equals(encoding, ignoreCase = true)) {
            //content 11 zlib 11
            decompressToStringForZlib(
                clone.readByteArray(),
                convertCharset(charset)
            )
        } else {
            //content 11111, 111111111111
            clone.readString(charset)
        }
    }

    enum class Level {
        /**
         * 111log
         */
        NONE,

        /**
         * 1111111
         */
        REQUEST,

        /**
         * 1111111
         */
        RESPONSE,

        /**
         * 11111111
         */
        ALL
    }

    companion object {
        /**
         * 111111111111
         *
         * @param request [Request]
         * @return 11111111
         * @throws UnsupportedEncodingException
         */
        @Throws(UnsupportedEncodingException::class)
        fun parseParams(request: Request): String {
            return try {
                val body = request.newBuilder().build().body() ?: return ""
                val requestbuffer = Buffer()
                body.writeTo(requestbuffer)
                var charset = Charset.forName("UTF-8")
                val contentType = body.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                var json = requestbuffer.readString(charset)
                if (hasUrlEncoded(json!!)) {
                    json = URLDecoder.decode(
                        json,
                        convertCharset(charset)
                    )
                }
                jsonFormat(json!!)
            } catch (e: Exception) {
                e.printStackTrace()
                "{\"error\": \"" + e.message + "\"}"
            }
        }

        /**
         * 111111
         *
         * @param mediaType [MediaType]
         * @return `true` 11111
         */
        fun isParseable(mediaType: MediaType?): Boolean {
            return if (mediaType?.type() == null) {
                false
            } else isText(mediaType) || isPlain(
                mediaType
            )
                    || isJson(mediaType) || isForm(
                mediaType
            )
                    || isHtml(mediaType) || isXml(
                mediaType
            )
        }

        fun isText(mediaType: MediaType?): Boolean {
            return if (mediaType?.type() == null) {
                false
            } else "text" == mediaType.type()
        }

        fun isPlain(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype()
                .lowercase(getDefault()).contains("plain")
        }

        @JvmStatic
        fun isJson(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().lowercase(Locale.getDefault()).contains("json")
        }

        @JvmStatic
        fun isXml(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().lowercase(Locale.getDefault()).contains("xml")
        }

        fun isHtml(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().lowercase(Locale.getDefault()).contains("html")
        }

        fun isForm(mediaType: MediaType?): Boolean {
            return if (mediaType?.subtype() == null) {
                false
            } else mediaType.subtype().lowercase(Locale.getDefault())
                .contains("x-www-form-urlencoded")
        }

        fun convertCharset(charset: Charset?): String {
            val s = charset.toString()
            val i = s.indexOf("[")
            return if (i == -1) {
                s
            } else s.substring(i + 1, s.length - 1)
        }
    }
}