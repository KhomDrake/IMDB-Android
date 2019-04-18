package com.example.imdb.data

import com.example.imdb.data.entity.http.Movie
import com.example.imdb.data.entity.http.MovieCredit
import com.example.imdb.data.entity.http.MovieDetail
import com.example.imdb.data.entity.http.Reviews

interface DataController {
    fun setupDatabase(language: String)
    fun getFavorites() : List<Movie>
    fun favoriteMovie(movieId: Int, favorite: Boolean)
    fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit)
    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit)
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit)
    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit)
    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit)
    fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit)
    fun loadPopular(funResponse: (movies: List<Movie>) -> Unit)
    fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit)
    fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit)
}