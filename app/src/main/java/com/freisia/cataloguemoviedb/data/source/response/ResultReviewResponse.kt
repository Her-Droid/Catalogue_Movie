package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class ResultReviewResponse (

    @SerializedName("id")
    val id: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("author_details")
    val authorDetails: DetailAuthorResponse,

    @SerializedName("content")
    val content: String,

    @SerializedName("updated_at")
    val updateDate: String
)