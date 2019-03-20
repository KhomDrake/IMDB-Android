package com.example.imdb.entity

import com.google.gson.annotations.SerializedName

data class MoviesList(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int
)