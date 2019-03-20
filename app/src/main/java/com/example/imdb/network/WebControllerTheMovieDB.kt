package com.example.imdb.network

import com.example.imdb.data.DataController
import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebControllerTheMovieDB {

    private val api = API().service()
    private lateinit var language: String
    private const val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"


    fun getLatest(funResponse: (body: Result) -> Unit) {
        api.getLatest(apiKey, DataController.getLanguage()).enqueue(requestResponse<Result>(funResponse))
    }

    fun getNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun getPopular(funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun getTopRated(funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun getUpcoming(funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    private fun <T> requestResponse(funResponse: (body: T) -> Unit) = object : Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) = Unit

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            response.body()?.apply(funResponse)
        }
    }


}