package com.example.imdb.network

import com.example.imdb.auxiliary.EMPTY_STRING
import com.example.imdb.auxiliary.PAGE_ONE
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.auxiliary.ZERO_DOUBLE
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class WebController(private val databaseMovies: DatabaseMovies) : IWebController {

    private val APIKEY = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    private val api = API().service()

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
        false, 0, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ZERO,
        EMPTY_STRING, ZERO_DOUBLE, ZERO, error = true
    )
    private val errorReview = Review(
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        error = true
    )
    private val errorReviews =
        Reviews(ZERO, ZERO, listOf(errorReview), ZERO, ZERO, ZERO)
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


    override fun loadLatest() {
        coroutine {
            try { funResponse(api.getLatestMovie(APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovie) }
        }
    }

    override fun loadNowPlaying() {
        coroutine {
            try { funResponse(api.getNowPlayingMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadPopular() {
        coroutine {
            try { funResponse(api.getPopularMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadTopRated() {
        coroutine {
            try { funResponse(api.getTopRatedMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadUpcoming() {
        coroutine {
            try { funResponse(api.getUpcomingMovie(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadMovieDetail(id: Int) {
        coroutine {
            try { funResponse(api.getDetailMovie(id, APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovieDetail) }
        }
    }

    override fun loadRecommendation(id: Int) {
        coroutine {
            try { funResponse(api.getRecommendationMovie(id, APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override suspend fun loadReviews(id: Int) {
        try { funResponse(api.getReviewsMovie(id, APIKEY, databaseMovies.getLanguage()).await()) }
        catch (e: Exception) { funResponse(errorReviews) }
    }

    override suspend fun loadMovieCredit(id: Int) : MovieCredit {
            try {
                return api.getMovieCredit(id, APIKEY).await()
            }
            catch (e: Exception) { return errorMovieCredit }
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            block()
        }
    }
}
