package com.example.imdb.data.entity.http.tv


import com.google.gson.annotations.SerializedName

data class Tv(
    val id: Int,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("poster_path")
    val posterPath: String
)