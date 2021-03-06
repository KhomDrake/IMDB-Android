package com.example.imdb.data.entity.http.movie

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    val title: String,
    val loading: Boolean,
    val error: Boolean,
    val adult: Boolean,
    var favorite: Boolean,
    val rating: Double = 0.0
)