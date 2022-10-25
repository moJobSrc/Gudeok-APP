package com.gudeok.gudeokapp.data

import com.google.gson.annotations.SerializedName
import java.util.*

class CommentData(@SerializedName("id") val id: Int,
                  @SerializedName("post_id") val postId: Int,
                  @SerializedName("message") val content: String,
                  @SerializedName("date") val date: String,
                  @SerializedName("parent_id") val parentId: Int,
                  @SerializedName("uuid") val uuid: String,
                  @SerializedName("author") val author: String)