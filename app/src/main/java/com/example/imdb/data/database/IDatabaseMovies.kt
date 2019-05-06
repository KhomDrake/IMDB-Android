package com.example.imdb.data.database

import com.example.imdb.MovieCategory
import com.example.imdb.data.entity.application.RightResponseMovieCategory
import com.example.imdb.data.entity.http.*

interface IDatabaseMovies {

    fun getLanguage() : String

    fun getMovieCredit(idMovie: Int, response: (MovieCredit) -> Unit)

    fun getLatest(response: (MutableList<Movie>) -> Unit)

    fun getNowPlaying(response: (MutableList<Movie>) -> Unit)

    fun getPopular(response: (MutableList<Movie>) -> Unit)

    fun getTopRated(response: (MutableList<Movie>) -> Unit)

    fun getUpcoming(response: (MutableList<Movie>) -> Unit)

    fun getDetailMovie(idMovie: Int, response: (MovieDetail) -> Unit)

    fun getRecommendationLastMovie(idMovie: Int, response: (Recommendation) -> Unit)

    fun getMovieReviews(_idMovie: Int, response: (Reviews) -> Unit)

    fun getLastTimeUpdateCategory(movieCategory: MovieCategory, response: (Long) -> Unit)

    fun getFavorites(response: (MutableList<Movie>) -> Unit)

    fun setReviews(reviews: Reviews)

    fun setRecommendationLastMovie(recommendation: Recommendation)

    fun setMovieDetail(movieDetail: MovieDetail)

    fun setLatest(movie: Movie, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                  rightResponseMovieCategory: RightResponseMovieCategory)

    fun setNowPlaying(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                      rightResponseMovieCategory: RightResponseMovieCategory)

    fun setPopular(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                   rightResponseMovieCategory: RightResponseMovieCategory)

    fun setTopRated(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                    rightResponseMovieCategory: RightResponseMovieCategory)

    fun setUpcoming(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                    rightResponseMovieCategory: RightResponseMovieCategory)

    fun setCreditMovie(credit: MovieCredit, idMovie: Int)

    fun lastTimeUpdateCategory(movieCategory: MovieCategory, currentTime: Long)

    fun favoriteMovie(movieId: Int, toFavorite: Boolean)
}