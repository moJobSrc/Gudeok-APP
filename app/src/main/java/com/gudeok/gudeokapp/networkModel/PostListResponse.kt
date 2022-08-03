package com.gudeok.gudeokapp.networkModel

import com.google.gson.annotations.SerializedName
import com.gudeok.gudeokapp.data.PostData

data class PostListResponse (
    @SerializedName("status") val status: Int,
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("bbslist") val postlist: ArrayList<PostData>
)