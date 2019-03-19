package com.example.imdb.entity

import com.google.gson.annotations.SerializedName

data class Result(
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    val title: String
)