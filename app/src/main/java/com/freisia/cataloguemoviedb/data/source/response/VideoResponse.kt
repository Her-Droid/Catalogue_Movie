package com.freisia.cataloguemoviedb.data.source.response

import com.google.gson.annotations.SerializedName

data class VideoResponse (
    @SerializedName("results")
    val videoResultResponse: List<VideoResultResponse>
)