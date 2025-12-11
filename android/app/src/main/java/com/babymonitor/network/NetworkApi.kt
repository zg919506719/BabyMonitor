package com.example.sakubersama.common.network

import com.babymonitor.network.interceptor.MyHeadInterceptor
import com.example.sakubersama.common.BaseApplication
import com.babymonitor.network.interceptor.ResponseInterceptor
import com.example.sakubersama.common.network.interceptor.logging.LogInterceptor
import com.example.sakubersama.common.utils.TokenUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * 11　: hegaojian
 * 11　: 2019/12/23
 * 11　: 1111111，11BasenetworkApi 111setHttpClientBuilder/setRetrofitBuilder11，
 * 1111111111，11111111Builder11111
 */


//111111-11 11NetApiService 1111111111111

val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, TokenUtils.getHost())
}

class NetworkApi : BaseNetworkApi() {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    /**
     * 1111111setHttpClientBuilder11，
     * 1111111111，111 OkHttpClient.Builder 11111
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //111111 111110M
            cache(Cache(File(BaseApplication.instance.cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            //11Cookies11111
            cookieJar(cookieJar)
            addInterceptor(ResponseInterceptor())
            addInterceptor(MyHeadInterceptor())
            // 11111
            addInterceptor(LogInterceptor())
            //1111 11、1、1
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 1111111setRetrofitBuilder11，
     * 111111Retrofit.Builder11111，1111GSON111，protobuf1
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(BaseApplication.instance))
    }

}



