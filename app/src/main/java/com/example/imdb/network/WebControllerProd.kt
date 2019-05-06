package com.example.imdb.network

import com.example.imdb.auxiliary.EMPTY_STRING
import com.example.imdb.auxiliary.PAGE_ONE
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.auxiliary.ZERO_DOUBLE
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.database.IDatabaseMovies
import com.example.imdb.data.entity.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class WebControllerProd(private val databaseMovies: IDatabaseMovies) : WebController {

    private val APIKEY = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    private val api = API().service()

    private val errorMovie = Movie(ZERO, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, loading = false, error = true, adult = false, favorite = false)
    private val errorMovieList = MoviesList(ZERO, listOf(errorMovie), ZERO)
    private val errorMovieDetail = MovieDetail(false, 0, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ZERO,
        EMPTY_STRING, ZERO_DOUBLE, ZERO, error = true)
    private val errorReview = Review(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, error = true)
    private val errorReviews = Reviews(ZERO, ZERO, listOf(errorReview), ZERO, ZERO, ZERO)
    private val errorCast = Cast(ZERO, EMPTY_STRING, ZERO, ZERO, EMPTY_STRING, ZERO, EMPTY_STRING, error = true)
    private val errorMovieCredit = MovieCredit(listOf(errorCast), ZERO)


    override fun loadLatest(funResponse: (body: Movie) -> Unit) {
        coroutine {
            try { funResponse(api.getLatest(APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovie) }
        }
    }

    override fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        coroutine {
            try { funResponse(api.getNowPlaying(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        coroutine {
            try { funResponse(api.getPopular(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        coroutine {
            try { funResponse(api.getTopRated(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        coroutine {
            try { funResponse(api.getUpcoming(APIKEY, databaseMovies.getLanguage(), PAGE_ONE).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadMovieDetail(id: Int, funResponse: (body: MovieDetail) -> Unit) {
        coroutine {
            try { funResponse(api.getDetail(id, APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovieDetail) }
        }
    }

    override fun loadRecommendation(id: Int, funResponse: (body: MoviesList) -> Unit) {
        coroutine {
            try { funResponse(api.getRecommendation(id, APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorMovieList) }
        }
    }

    override fun loadReviews(id: Int, funResponse: (body: Reviews) -> Unit) {
        coroutine {
            try { funResponse(api.getReviews(id, APIKEY, databaseMovies.getLanguage()).await()) }
            catch (e: Exception) { funResponse(errorReviews) }
        }
    }

    override fun loadMovieCredit(id: Int, funResponse: (body: MovieCredit) -> Unit) {
        coroutine {
            try { funResponse(api.getMovieCredit(id, APIKEY).await()) }
            catch (e: Exception) { funResponse(errorMovieCredit) }
        }
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            block()
        }
    }
}
