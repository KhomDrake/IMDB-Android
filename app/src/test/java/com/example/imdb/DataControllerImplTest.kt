package com.example.imdb

import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.data.entity.http.MovieCredit
import com.example.imdb.data.entity.http.MovieDetail
import com.example.imdb.data.entity.http.Reviews

class DataControllerImplTest: IDataController {
    override fun setupDatabase(language: String) {
    }

    override fun getFavorites(response: (MutableList<Movie>) -> Unit) {
    }

    override fun favoriteMovie(movieId: Int, toFavorite: Boolean) {
    }

    override fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
    }

    override fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
    }

    override fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
    }

    override fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
    }

    override fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
    }

    override fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
    }

    override fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
    }

    override fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
    }

    override fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        val movie = Movie(0, "", "", "", false, false, false ,false)
        funResponse(listOf(movie, movie, movie))
    }
}