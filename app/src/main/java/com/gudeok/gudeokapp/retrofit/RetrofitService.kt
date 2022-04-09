package com.gudeok.gudeokapp.retrofit

import com.gudeok.gudeokapp.networkModel.*
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

    @POST("/login")
    fun loginRequest(
        @Query("id") id: String,
        @Query("pw") pw: String
    ): Call<LogInCheckOkResponse>

    @POST("/register")
    fun registerRequest(
        @Query("id") id: String,
        @Query("pw") pw: String
    ): Call<ResponseDTO>

    @POST("/bbslist")
    fun loadbbsList(
        @Query("page") id: Int
    ): Call<PostListResponse>

    @POST("/bbsGet")
    fun seePost(
        @Query("id") id: Int
    ): Call<PostResponse>

    @POST("/check")
    fun tokenCheckRequest(): Call<TokenCheckResponse>

}