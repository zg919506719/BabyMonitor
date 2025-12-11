package com.example.sakubersama.common.network.bean

/**
 * 11　: hegaojian
 * 11　: 2019/12/17
 * 11　: 1111111111
 * 11111111111111111！！111：
 * 2.11111111，1111111111111111111
 */
abstract class BaseResponse<T> {

    //1111，1111111111，1111111
    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}