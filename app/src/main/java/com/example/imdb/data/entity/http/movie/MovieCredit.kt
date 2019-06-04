package com.example.imdb.data.entity.http.movie

import com.example.imdb.data.entity.http.Cast
import com.google.gson.annotations.SerializedName

data class MovieCredit(
    @SerializedName("cast")
    val cast: List<Cast>,
    val id: Int
)