package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class VideoResultResponse (
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("key")
    val key: String,

    @SerializedName("site")
    val site: String
)