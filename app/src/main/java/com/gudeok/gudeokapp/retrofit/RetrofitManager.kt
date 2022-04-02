package com.gudeok.gudeokapp.retrofit

import android.util.Log
import com.gudeok.gudeokapp.util.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val baseURL = "http://39.113.240.156:3000/"

    //retrofit 클라이언트 구성
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).addInterceptor(AuthInterceptor()).build())
        .build()

    private val retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)

    fun getClient(): RetrofitService {
        //클라이언트 리턴
        return retrofitService
    }

}

class AuthInterceptor : Interceptor {
    //retrofit 클라이언트에 토큰값을 담아줌
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Token", App.prefs.token?: "")
        val req =
            chain.request().newBuilder().addHeader("Authorization", "Bearer ${App.prefs.token}").build()
        return chain.proceed(req)
    }
}