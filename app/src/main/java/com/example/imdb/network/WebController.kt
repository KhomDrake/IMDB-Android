package com.example.imdb.network

import com.example.imdb.auxiliary.EMPTY_STRING
import com.example.imdb.auxiliary.PAGE_ONE
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.auxiliary.ZERO_DOUBLE
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebController {

    private const val API_KEY = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    private val api = API().service()

    private val errorMovie = Movie(ZERO, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, loading = false, error = true, adult = false,
            favorite = false)
    private val errorMovieList = MoviesList(ZERO, listOf(errorMovie), ZERO)
    private val errorMovieDetail = MovieDetail(false, 0, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ZERO,
        EMPTY_STRING, ZERO_DOUBLE, ZERO, true)
    private val errorReview = Review(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, true)
    private val errorReviews = Reviews(ZERO, ZERO, listOf(errorReview), ZERO, ZERO, ZERO)
    private val errorCast = Cast(ZERO, EMPTY_STRING, ZERO, ZERO, EMPTY_STRING, ZERO, EMPTY_STRING, true)
    private val errorMovieCredit = MovieCredit(listOf(errorCast), ZERO)


    fun loadLatest(funResponse: (body: Movie) -> Unit) {
        api.getLatest(API_KEY, DataController.getLanguage())
            .enqueue(requestResponse<Movie>(funResponse, errorMovie))
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(API_KEY, DataController.getLanguage(), PAGE_ONE)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(API_KEY, DataController.getLanguage(), PAGE_ONE)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(API_KEY, DataController.getLanguage(), PAGE_ONE)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(API_KEY, DataController.getLanguage(), PAGE_ONE)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadMovieDetail(id: Int, funResponse: (body: MovieDetail) -> Unit) {
        api.getDetail(id, API_KEY, DataController.getLanguage())
            .enqueue(requestResponse<MovieDetail>(funResponse, errorMovieDetail))
    }

    fun loadRecommendation(id: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getRecommendation(id, API_KEY, DataController.getLanguage())
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadReviews(id: Int, funResponse: (body: Reviews) -> Unit) {
        api.getReviews(id, API_KEY, DataController.getLanguage())
            .enqueue(requestResponse<Reviews>(funResponse, errorReviews))
    }

    fun loadMovieCredit(id: Int, funResponse: (body: MovieCredit) -> Unit) {
        api.getMovieCredit(id, API_KEY)
            .enqueue(requestResponse<MovieCredit>(funResponse, errorMovieCredit))
    }

    private fun <T>requestResponse(funResponse: (body: T) -> Unit, error: T?) = object : Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) {
            funResponse(error!!)
        }

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            if(response.body() == null)
                funResponse(error!!)
            else
                response.body()?.apply(funResponse)
        }
    }
}