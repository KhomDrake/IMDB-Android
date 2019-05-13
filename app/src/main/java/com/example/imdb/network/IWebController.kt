package com.example.imdb.network

import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*

interface IWebController {

    suspend fun loadLatest() : Movie
    suspend fun loadNowPlaying() : Movi
    suspend fun loadPopular()
    suspend fun loadTopRated()
    suspend fun loadUpcoming()
    suspend fun loadMovieDetail(id: Int)
    suspend fun loadRecommendation(id: Int)
    suspend fun loadReviews(id: Int)
    suspend fun loadMovieCredit(id: Int) : MovieCredit
}