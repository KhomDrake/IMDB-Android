package com.example.imdb.data.entity.http.tv

import com.google.gson.annotations.SerializedName

data class TvList(
    val page: Int,
    @SerializedName("results")
    val tvs: List<Tv>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)