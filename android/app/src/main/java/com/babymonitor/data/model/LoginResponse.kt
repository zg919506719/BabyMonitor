package com.babymonitor.data.model

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "userId") val userId: String,
    @Json(name = "token") val token: String,
    @Json(name = "userName") val userName: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "babyInfo") val babyInfo: BabyInfo?
)

data class BabyInfo(
    @Json(name = "babyName") val babyName: String,
    @Json(name = "babyAge") val babyAge: Int,
    @Json(name = "babyGender") val babyGender: Int // 0: 男, 1: 女
)