package com.example.imdb.entity.NowPlaying

import com.google.gson.annotations.SerializedName

data class NowPlaying(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)