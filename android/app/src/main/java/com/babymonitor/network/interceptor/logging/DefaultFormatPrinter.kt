package com.example.sakubersama.common.network.interceptor.logging

import android.text.TextUtils
import com.example.sakubersama.common.network.interceptor.logging.LogInterceptor.Companion.isJson
import com.example.sakubersama.common.network.interceptor.logging.LogInterceptor.Companion.isXml
import com.example.sakubersama.common.utils.CharacterHandler.Companion.jsonFormat
import com.example.sakubersama.common.utils.CharacterHandler.Companion.xmlFormat
import com.example.sakubersama.common.utils.LogUtils
import okhttp3.MediaType
import okhttp3.Request
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 11　: hegaojian
 * 11　: 2020/3/26
 * 11　:
 */
class DefaultFormatPrinter : FormatPrinter {

    private var appendTag = ""

    /**
     * 11111111, 111111 {[okhttp3.RequestBody]} 1111111
     *
     * @param request
     * @param bodyString
     */
    override fun printJsonRequest(
        request: Request,
        bodyString: String
    ) {
        appendTag = md5(URL_TAG + request.url())
        val requestBody =
            LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyString
        val tag = getTag(true)
        LogUtils.debugInfo(tag, REQUEST_UP_LINE)
        logLines(
            tag,
            arrayOf(URL_TAG + request.url()),
            false
        )
        logLines(
            tag,
            getRequest(request),
            true
        )
        logLines(
            tag,
            requestBody.split(LINE_SEPARATOR!!).toTypedArray(),
            true
        )
        LogUtils.debugInfo(tag, END_LINE)
    }

    /**
     * 11111111, 111111 {[okhttp3.RequestBody]} 1 `null` 11111111
     *
     * @param request
     */
    override fun printFileRequest(request: Request) {
        appendTag = md5(URL_TAG + request.url())
        val tag = getTag(true)
        LogUtils.debugInfo(tag, REQUEST_UP_LINE)
        logLines(
            tag,
            arrayOf(URL_TAG + request.url()),
            false
        )
        logLines(
            tag,
            getRequest(request),
            true
        )
        logLines(
            tag,
            OMITTED_REQUEST,
            true
        )
        LogUtils.debugInfo(tag, END_LINE)
    }

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
    override fun printJsonResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        contentType: MediaType?,
        bodyString: String?,
        segments: List<String?>,
        message: String,
        responseUrl: String
    ) {
        appendTag = md5(URL_TAG + responseUrl)
        var bodyString = bodyString
        bodyString =
            when {
                isJson(contentType) -> jsonFormat(bodyString!!)
                isXml(
                    contentType
                ) -> xmlFormat(bodyString)
                else -> bodyString
            }
        val responseBody =
            LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyString
        val tag = getTag(false)
        val urlLine = arrayOf<String?>(
            URL_TAG + responseUrl,
            N
        )
        LogUtils.debugInfo(tag, RESPONSE_UP_LINE)
        logLines(tag, urlLine, true)
        logLines(
            tag,
            getResponse(
                headers,
                chainMs,
                code,
                isSuccessful,
                segments,
                message
            ),
            true
        )
        logLines(
            tag,
            responseBody.split(LINE_SEPARATOR!!).toTypedArray(),
            true
        )
        LogUtils.debugInfo(tag, END_LINE)
    }

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
    override fun printFileResponse(
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        segments: List<String?>,
        message: String,
        responseUrl: String
    ) {
        appendTag = md5(URL_TAG + responseUrl)
        val tag = getTag(false)
        val urlLine = arrayOf<String?>(
            URL_TAG + responseUrl,
            N
        )
        LogUtils.debugInfo(tag, RESPONSE_UP_LINE)
        logLines(tag, urlLine, true)
        logLines(
            tag,
            getResponse(
                headers,
                chainMs,
                code,
                isSuccessful,
                segments,
                message
            ),
            true
        )
        logLines(
            tag,
            OMITTED_RESPONSE,
            true
        )
        LogUtils.debugInfo(tag, END_LINE)
    }

    private fun getTag(isRequest: Boolean): String {
        return if (isRequest) {
            "$TAG-Request-$appendTag"
        } else {
            "$TAG-Response-$appendTag"
        }
    }

    companion object {
        private const val TAG = "HttpLog"
        private val LINE_SEPARATOR = System.getProperty("line.separator")
        private val DOUBLE_SEPARATOR =
            LINE_SEPARATOR + LINE_SEPARATOR
        private val OMITTED_RESPONSE = arrayOf(
            LINE_SEPARATOR,
            "Omitted response body"
        )
        private val OMITTED_REQUEST = arrayOf(
            LINE_SEPARATOR,
            "Omitted request body"
        )
        private const val N = "\n"
        private const val T = "\t"
        private const val REQUEST_UP_LINE =
            "   ┌────── Request ────────────────────────────────────────────────────────────────────────"
        private const val END_LINE =
            "   └───────────────────────────────────────────────────────────────────────────────────────"
        private const val RESPONSE_UP_LINE =
            "   ┌────── Response ───────────────────────────────────────────────────────────────────────"
        private const val BODY_TAG = "Body:"
        private const val URL_TAG = "URL: "
        private const val METHOD_TAG = "Method: @"
        private const val HEADERS_TAG = "Headers:"
        private const val STATUS_CODE_TAG = "Status Code: "
        private const val RECEIVED_TAG = "Received in: "
        private const val CORNER_UP = "┌ "
        private const val CORNER_BOTTOM = "└ "
        private const val CENTER_LINE = "├ "
        private const val DEFAULT_LINE = "│ "
        private val ARMS =
            arrayOf("-A-", "-R-", "-M-", "-S-")
        private val last: ThreadLocal<Int> = object : ThreadLocal<Int>() {
            override fun initialValue(): Int {
                return 0
            }
        }

        private fun isEmpty(line: String): Boolean {
            return TextUtils.isEmpty(line) || N == line || T == line || TextUtils.isEmpty(
                line.trim { it <= ' ' }
            )
        }

        /**
         * 1 `lines` 1111111111
         *
         * @param tag
         * @param lines
         * @param withLineSize 1 `true` 1, 11111111111110, 1111111
         */
        private fun logLines(
            tag: String,
            lines: Array<String?>,
            withLineSize: Boolean
        ) {
//            for (line in lines) {
//                val lineLength = line!!.length
//                val maxLongSize = if (withLineSize) 110 else lineLength
//                for (i in 0..lineLength / maxLongSize) {
//                    val start = i * maxLongSize
//                    var end = (i + 1) * maxLongSize
//                    end = if (end > line.length) line.length else end
//                    LogUtils.debugInfo(
//                        resolveTag(tag),
//                        DEFAULT_LINE + line.substring(start, end)
//                    )
//                }
//            }
            val lineBuilder = java.lang.StringBuilder()
            for (itemLine in lines) {
                lineBuilder.append(itemLine)
                lineBuilder.append("\n")
            }
            val line = lineBuilder.toString()
            LogUtils.debugInfo(resolveTag(tag), DEFAULT_LINE + line)
        }

        private fun computeKey(): String {
            if (last.get()!! >= 4) {
                last.set(0)
            }
            val s =
                ARMS[last.get()!!]
            last.set(last.get()!! + 1)
            return s
        }

        /**
         * 111111111 AndroidStudio v3.1 11 Logcat 111111111111
         *
         *
         * 11111111, 1 JessYan 11, 11111 AndroidStudio v3.1 11111111111 tag 11111 log 111111111
         * 11111111111, 11111111
         * AndroidStudio v3.1 1111111111, 1111111111111111111111111111111
         * 111111111111111: 1. 11111 tag (11 tag 11111111 token) 2. 1111111111111
         *
         *
         * [.resolveTag] 111111111
         *
         * @param tag
         */
        private fun resolveTag(tag: String): String {
            return computeKey() + tag
        }

        private fun getRequest(request: Request): Array<String?> {
            val log: String
            val header = request.headers().toString()
            log =
                METHOD_TAG + request.method() + DOUBLE_SEPARATOR +
                        if (isEmpty(header)) "" else HEADERS_TAG + LINE_SEPARATOR + dotHeaders(
                            header
                        )
            return log.split(LINE_SEPARATOR!!).toTypedArray()
        }

        private fun getResponse(
            header: String, tookMs: Long, code: Int, isSuccessful: Boolean,
            segments: List<String?>, message: String
        ): Array<String?> {
            val log: String
            val segmentString =
                slashSegments(segments)
            log =
                ((if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : "
                        + isSuccessful + " - " + RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG +
                        code + " / " + message + DOUBLE_SEPARATOR + if (isEmpty(
                        header
                    )
                ) "" else HEADERS_TAG + LINE_SEPARATOR +
                        dotHeaders(header))
            return log.split(LINE_SEPARATOR!!).toTypedArray()
        }

        private fun slashSegments(segments: List<String?>): String {
            val segmentString = StringBuilder()
            for (segment in segments) {
                segmentString.append("/").append(segment)
            }
            return segmentString.toString()
        }

        /**
         * 1 `header` 1111111111
         *
         * @param header
         * @return
         */
        private fun dotHeaders(header: String): String {
            val headers =
                header.split(LINE_SEPARATOR!!).toTypedArray()
            val builder = StringBuilder()
            var tag = "─ "
            if (headers.size > 1) {
                for (i in headers.indices) {
                    tag = if (i == 0) {
                        CORNER_UP
                    } else if (i == headers.size - 1) {
                        CORNER_BOTTOM
                    } else {
                        CENTER_LINE
                    }
                    builder.append(tag).append(headers[i]).append("\n")
                }
            } else {
                for (item in headers) {
                    builder.append(tag).append(item).append("\n")
                }
            }
            return builder.toString()
        }

        /**
         * md511
         */
        private fun md5(string: String): String {
            if (TextUtils.isEmpty(string)) {
                return ""
            }
            val md5: MessageDigest
            try {
                md5 = MessageDigest.getInstance("MD5")
                val bytes = md5.digest(string.toByteArray())
                val result = java.lang.StringBuilder()
                for (b in bytes) {
                    var temp = Integer.toHexString(b.toInt() and 0xff)
                    if (temp.length == 1) {
                        temp = "0$temp"
                    }
                    result.append(temp)
                }
                return result.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}