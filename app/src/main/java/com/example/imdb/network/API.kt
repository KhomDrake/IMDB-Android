package com.example.imdb.network

import com.example.imdb.EMPTY_STRING
import com.example.imdb.PAGE_ONE
import com.example.imdb.ZERO
import com.example.imdb.ZERO_DOUBLE
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.ui.MovieDbCategory
import java.lang.Exception

class API(private val databaseMovies: DatabaseMovies) {

    private val APIKEY = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    private val api = TheMovieDBAPI().service()

    private val errorMovie = Movie(
        ZERO,
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )
    private val errorMovieList = MoviesList(ZERO, listOf(errorMovie), ZERO)
    private val errorMovieDetail = MovieDetail(
        false,
        ZERO,
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        ZERO,
        EMPTY_STRING,
        ZERO_DOUBLE,
        ZERO, error = true
    )
    private val errorReview = Review(
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        error = true
    )
    private val errorReviews =
        Reviews(
            ZERO,
            ZERO, listOf(errorReview),
            ZERO,
            ZERO,
            ZERO
        )
    private val errorCast = CastMovie(
        ZERO,
        EMPTY_STRING,
        ZERO,
        ZERO,
        EMPTY_STRING,
        ZERO,
        EMPTY_STRING,
        error = true
    )
    private val errorMovieCredit = MovieCredit(listOf(errorCast), ZERO)

    suspend fun loadCategory(movieDbCategory: MovieDbCategory) : MoviesList {
        return try {
            when(movieDbCategory) {
                MovieDbCategory.MovieLatest -> MoviesList(
                    ZERO,
                    listOf(api.getLatestMovie(APIKEY, databaseMovies.getLanguage()).await()),
                    ZERO)
                MovieDbCategory.MovieNowPlaying -> api.getNowPlayingMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()
                MovieDbCategory.MoviePopular -> api.getPopularMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()
                MovieDbCategory.MovieUpcoming -> api.getUpcomingMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()
                MovieDbCategory.MovieTopRated -> api.getTopRatedMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()
                else -> errorMovieList
            }
        } catch (e: Exception) { errorMovieList }
    }

    suspend fun loadMovieDetail(id: Int) : MovieDetail {
        return try { api.getDetailMovie(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorMovieDetail }
    }

    suspend fun loadRecommendation(id: Int) : MoviesList {
        return try { api.getRecommendationMovie(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorMovieList }
    }

    suspend fun loadReviews(id: Int) : Reviews {
        return try { api.getReviewsMovie(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorReviews }
    }

    suspend fun loadMovieCredit(id: Int) : MovieCredit {
            try {
                return api.getMovieCredit(id, APIKEY).await()
            }
            catch (e: Exception) { return errorMovieCredit }
    }
}
