package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class DataGenresResponse (
    @SerializedName("genres")
    val genreResponse: List<GenreResponse>
)