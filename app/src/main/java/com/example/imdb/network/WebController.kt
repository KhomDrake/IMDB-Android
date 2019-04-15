package com.example.imdb.network

import android.util.Log
import com.example.imdb.TAG_VINI
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebController {

    private val api = API().service()
    private const val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"
    private val errorMovie =
        Movie(0, "asd", "asd", "asd", loading = false, error = true, adult = false, favorite = false)
    private val errorMovieList = MoviesList(0, listOf(errorMovie), 0)
    private val errorMovieDetail = MovieDetail(false, 0, "", "", "", 0, "", 0.0, 0, true)
    private val errorReview = Review("asd", "", "", "", true)
    private val errorReviews = Reviews(0, 0, listOf(), 0, 0, 0)
    private val errorCast = Cast(2, "asd", 0, 0, "", 0, "", true)
    private val errorMovieCredit = MovieCredit(listOf(errorCast), 0)


    fun loadLatest(funResponse: (body: Movie) -> Unit) {
        api.getLatest(apiKey, DataController.getLanguage())
            .enqueue(requestResponse<Movie>(funResponse, errorMovie))
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(apiKey, DataController.getLanguage(), 1)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(apiKey, DataController.getLanguage(), 1)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(apiKey, DataController.getLanguage(), 1)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(apiKey, DataController.getLanguage(), 1)
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadMovieDetail(id: Int, funResponse: (body: MovieDetail) -> Unit) {
        api.getDetail(id, apiKey, DataController.getLanguage())
            .enqueue(requestResponse<MovieDetail>(funResponse, errorMovieDetail))
    }

    fun loadRecommendation(id: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getRecommendation(id, apiKey, DataController.getLanguage())
            .enqueue(requestResponse<MoviesList>(funResponse, errorMovieList))
    }

    fun loadReviews(id: Int, funResponse: (body: Reviews) -> Unit) {
        api.getReviews(id, apiKey, DataController.getLanguage())
            .enqueue(requestResponse<Reviews>(funResponse, errorReviews))
    }

    fun loadMovieCredit(id: Int, funResponse: (body: MovieCredit) -> Unit) {
        api.getMovieCredit(id, apiKey)
            .enqueue(requestResponse<MovieCredit>(funResponse, errorMovieCredit))
    }

    private fun <T>requestResponse(funResponse: (body: T) -> Unit, error: T?) = object : Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) {
            Log.i(TAG_VINI, t.message)
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