package com.example.imdb.data.entity.http.movie

import com.google.gson.annotations.SerializedName

data class MovieCredit(
    @SerializedName("cast")
    val castMovie: List<CastMovie>,
    val id: Int
)