package com.example.imdb.entity

import com.example.imdb.entity.Result
import com.google.gson.annotations.SerializedName

data class NowPlaying(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)