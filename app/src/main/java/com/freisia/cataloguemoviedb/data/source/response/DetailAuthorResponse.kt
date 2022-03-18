package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class DetailAuthorResponse(

    @SerializedName("avatar_path")
    val image: String?,

    @SerializedName("rating")
    val rating: Float
)