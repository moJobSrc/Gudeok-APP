package com.gudeok.gudeokapp.networkModel

import com.google.gson.annotations.SerializedName
import com.gudeok.gudeokapp.data.PostData

data class PostWriteResponse (
    @SerializedName("status") val status: Int,
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("bbsId") val bbsId: Int?
)