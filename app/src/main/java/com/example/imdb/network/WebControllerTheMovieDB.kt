package com.example.imdb.network

import com.example.imdb.data.DataController
import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebControllerTheMovieDB {

    private val api = API().service()
    private lateinit var language: String
    private const val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"


    fun loadLatest(funResponse: (body: Movie) -> Unit) {
        api.getLatest(apiKey, DataController.getLanguage()).enqueue(requestResponse<Movie>(funResponse))
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadNowPlaying(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(apiKey, DataController.getLanguage(), pag).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadPopular(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(apiKey, DataController.getLanguage(), pag).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadTopRated(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(apiKey, DataController.getLanguage(), pag).enqueue(requestResponse<MoviesList>(funResponse))
    }

    fun loadUpcoming(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(apiKey, DataController.getLanguage(), pag).enqueue(requestResponse<MoviesList>(funResponse))
    }


    private fun <T> requestResponse(funResponse: (body: T) -> Unit) = object : Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) = Unit

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            response.body()?.apply(funResponse)
        }
    }


}