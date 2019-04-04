package com.example.imdb.data.entity.http

import com.google.gson.annotations.SerializedName

data class MoviesList(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int
)