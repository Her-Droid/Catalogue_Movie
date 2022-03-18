package com.freisia.cataloguemoviedb.domain.model

data class Review (
    val id: String,
    val author: String,
    val image: String?,
    val rating: Float,
    val content: String,
    val updateDate: String
)