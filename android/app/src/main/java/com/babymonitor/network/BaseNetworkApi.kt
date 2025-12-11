package com.example.sakubersama.common.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 11　: hegaojian
 * 11　: 2019/12/23
 * 11　: 111111111
 */
abstract class BaseNetworkApi {

    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    /**
     * 1111111setHttpClientBuilder11，
     * 1111111111，111 OkHttpClient.Builder 11111
     */
    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    /**
     * 1111111setRetrofitBuilder11，
     * 111111Retrofit.Builder11111，1111GSON111，Protocol
     */
    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder

    /**
     * 11http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder = OkHttpClient.Builder()
            builder = setHttpClientBuilder(builder)
            return builder.build()
        }
}



