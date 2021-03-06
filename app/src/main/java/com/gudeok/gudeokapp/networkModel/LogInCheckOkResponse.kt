package com.gudeok.gudeokapp.networkModel

import com.google.gson.annotations.SerializedName

data class LogInCheckOkResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("accessToken") val accessToken: String
)
