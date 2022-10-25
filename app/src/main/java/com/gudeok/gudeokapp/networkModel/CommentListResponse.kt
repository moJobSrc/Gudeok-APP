package com.gudeok.gudeokapp.networkModel

import com.google.gson.annotations.SerializedName
import com.gudeok.gudeokapp.data.CommentData
import com.gudeok.gudeokapp.data.PostData

data class CommentListResponse (
    @SerializedName("status") val status: Int,
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("commentList") val commentList: ArrayList<CommentData>
)