package com.example.imdb.data.entity.http.tv


import com.google.gson.annotations.SerializedName

data class TVList(
    val page: Int,
    val tvs: List<TV>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)