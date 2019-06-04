package com.example.imdb.data.entity.http.tv


data class TVCredit(
    val castTV: List<CastTV>,
    val crew: List<Crew>,
    val id: Int
)