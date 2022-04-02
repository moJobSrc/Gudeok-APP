package com.gudeok.gudeokapp.networkModel

import com.google.gson.annotations.SerializedName

data class TokenCheckResponse(
    @SerializedName("ok") val ok: Boolean
)
