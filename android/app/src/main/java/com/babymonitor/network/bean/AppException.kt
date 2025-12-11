package com.example.sakubersama.common.network.bean

/**
 * 11　: hegaojian
 * 11　: 2019/12/17
 * 11　:111111111
 */
class AppException : Exception {

    var errorMsg: String //1111
    var errCode: Int = 0 //111
    var errorLog: String? //1111
    var throwable: Throwable? = null

    constructor(errCode: Int, error: String?, errorLog: String? = "", throwable: Throwable? = null) : super(error) {
        this.errorMsg = error ?: "Net Error"
        this.errCode = errCode
        this.errorLog = errorLog ?: this.errorMsg
        this.throwable = throwable
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
        throwable = e
    }
}