package com.babymonitor.data.repository

import com.babymonitor.data.model.LoginResponse
import com.babymonitor.data.remote.ApiService
import com.babymonitor.data.remote.RetrofitClient

class UserRepository {

    private val apiService: ApiService by lazy {
        RetrofitClient.createService(ApiService::class.java)
    }

    private val userPreference = UserPreference()

    suspend fun login(phone: String, password: String): LoginResponse {
        return apiService.login(phone, password)
    }

    fun saveUserInfo(response: LoginResponse) {
        userPreference.saveToken(response.token)
        userPreference.saveUserId(response.userId)
    }

    fun isLoggedIn(): Boolean {
        return userPreference.getToken().isNotEmpty()
    }
}