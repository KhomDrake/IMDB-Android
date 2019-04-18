package com.example.imdb.network

import com.example.imdb.data.entity.http.*

interface WebController {

    fun loadLatest(funResponse: (body: Movie) -> Unit)
    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit)
    fun loadPopular(funResponse: (body: MoviesList) -> Unit)
    fun loadTopRated(funResponse: (body: MoviesList) -> Unit)
    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit)
    fun loadMovieDetail(id: Int, funResponse: (body: MovieDetail) -> Unit)
    fun loadRecommendation(id: Int, funResponse: (body: MoviesList) -> Unit)
    fun loadReviews(id: Int, funResponse: (body: Reviews) -> Unit)
    fun loadMovieCredit(id: Int, funResponse: (body: MovieCredit) -> Unit)
}