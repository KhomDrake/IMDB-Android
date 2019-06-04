package com.example.imdb.data.entity.http.tv


import com.example.imdb.data.entity.http.Cast
import com.example.imdb.data.entity.http.tv.Crew

data class TvCredit(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)