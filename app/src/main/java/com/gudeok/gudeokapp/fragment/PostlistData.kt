package com.gudeok.gudeokapp.fragment

import com.google.gson.annotations.SerializedName
import java.util.*

class PostlistData(@SerializedName("id") val id: Int,
                   @SerializedName("title") val title: String,
                   @SerializedName("author") val author: String,
                   @SerializedName("date") val date: String,
                   @SerializedName("content") val content: String,
                   @SerializedName("uuid") val uuid: String,
                   @SerializedName("images") val images: String,
                   @SerializedName("gaechu") val gaechu: Int,
                   @SerializedName("beechu") val beechu: Int,
                   @SerializedName("seen") val seen: Int)