package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class DataResponse <T> (
    @SerializedName("results")
    val listData: List<T>,

    @SerializedName("total_pages")
    val totalPage: Int,

    @SerializedName("page")
    val page: Int
)