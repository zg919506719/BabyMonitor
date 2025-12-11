package com.example.sakubersama.common.network.bean

import com.google.gson.annotations.SerializedName


/**
 * 11　: hegaojian
 * 11　: 2019/12/23
 * 11　:1111111111
 * 1111111111，1111，1111BaseResponse，1111111111111，1111111111，111：
 * 1.11 BaseResponse
 * 2.11isSucces 11，11111111，11111111111111111
 * 3.11 getResponseCode、getResponseData、getResponseMsg11，1111 code data msg
 */

data class ApiResponse<T>(
    @SerializedName(value = "error_code", alternate = ["code"])
    val code: Int,
    @SerializedName(value = "error_msg", alternate = ["message"])
    val message: String,
    @SerializedName(value = "result", alternate = ["data"])
    val data: T
) : BaseResponse<T>() {

     override fun isSucces() = code == 0 || code == 200
    /**
     * app-back1111110 11200110
     * @return Int
     */
    override fun getResponseCode(): Int {
        return if (code == 200) {
            0
        } else code
    }


    override fun getResponseData() = data

    override fun getResponseMsg() = message

}
