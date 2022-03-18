package com.freisia.cataloguemoviedb.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String?,
    val image: String?,
    val backdropImage: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val status: String,
    val genre: List<Genre>
)