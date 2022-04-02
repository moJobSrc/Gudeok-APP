package com.gudeok.gudeokapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitManager {
    private const val baseURL = "http://39.113.240.156:3000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
//        .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor())
        .build()

    private val server: RetrofitService = retrofit.create(RetrofitService::class.java)

    fun getClient(): RetrofitService {
        return server
    }

}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req =
            chain.request().newBuilder().addHeader("Bearer ", App.prefs.token ?: "").build()
        return chain.proceed(req)
    }
}