package com.example.imdb.network

import com.example.imdb.ui.EMPTY_STRING
import com.example.imdb.ui.ZERO
import com.example.imdb.ui.ZERO_DOUBLE
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.Cast
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.ui.TheMovieDbCategory
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
    private val errorCast = Cast(
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

    suspend fun loadCategory(theMovieDbCategory: TheMovieDbCategory, page: Int = 1) : MoviesList {
        return try {
            when(theMovieDbCategory) {
                TheMovieDbCategory.MovieLatest -> MoviesList(
                    ZERO,
                    listOf(api.getLatestMovieAsync(APIKEY, databaseMovies.getLanguage()).await()),
                    ZERO
                )
                TheMovieDbCategory.MovieNowPlaying -> api.getNowPlayingMovieAsync(APIKEY, databaseMovies.getLanguage(),
                    page
                ).await()
                TheMovieDbCategory.MoviePopular -> api.getPopularMovieAsync(APIKEY, databaseMovies.getLanguage(),
                    page
                ).await()
                TheMovieDbCategory.MovieUpcoming -> api.getUpcomingMovieAsync(APIKEY, databaseMovies.getLanguage(),
                    page
                ).await()
                TheMovieDbCategory.MovieTopRated -> api.getTopRatedMovieAsync(APIKEY, databaseMovies.getLanguage(),
                    page
                ).await()
                else -> errorMovieList
            }
        } catch (e: Exception) { errorMovieList }
    }

    suspend fun loadMovieDetail(id: Int) : MovieDetail {
        return try { api.getDetailMovieAsync(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorMovieDetail }
    }

    suspend fun loadRecommendation(id: Int) : MoviesList {
        return try { api.getRecommendationMovieAsync(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorMovieList }
    }

    suspend fun loadReviews(id: Int) : Reviews {
        return try { api.getReviewsMovieAsync(id, APIKEY, databaseMovies.getLanguage()).await() }
            catch (e: Exception) { errorReviews }
    }

    suspend fun loadMovieCredit(id: Int) : MovieCredit {
        return try { api.getMovieCreditAsync(id, APIKEY).await() }
            catch (e: Exception) { errorMovieCredit }
    }
}
