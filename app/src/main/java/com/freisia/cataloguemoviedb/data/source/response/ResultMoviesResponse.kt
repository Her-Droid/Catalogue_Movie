package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class ResultMoviesResponse (
    @SerializedName("id")
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("poster_path")
    val image: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
)