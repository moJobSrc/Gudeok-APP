package com.gudeok.gudeokapp

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

}