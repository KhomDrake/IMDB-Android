package com.example.imdb.network

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.MoviesList
import com.example.imdb.data.entity.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WebController {

    private val api = API().service()
    private const val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    fun loadLatest(funResponse: (body: Movie) -> Unit) {
        api.getLatest(apiKey, DataController.getLanguage()).enqueue(requestResponse(funResponse))
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        api.getNowPlaying(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse(funResponse))
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        api.getPopular(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse(funResponse))
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        api.getTopRated(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse(funResponse))
    }

    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        api.getUpcoming(apiKey, DataController.getLanguage(), 1).enqueue(requestResponse(funResponse))
    }

    private fun requestResponse(funResponse: (body: Movie) -> Unit) = object : Callback<Movie> {
        override fun onFailure(call: Call<Movie?>, t: Throwable) {
            funResponse(Movie(0, "asd", "asd", "asd", loading = false, error = true))
        }

        override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
            response.body()?.apply(funResponse)
        }
    }

    private fun requestResponse(funResponse: (body: MoviesList) -> Unit) = object : Callback<MoviesList> {
        override fun onFailure(call: Call<MoviesList?>, t: Throwable) {
            val m = Movie(0, "asd", "asd", "asd", loading = false, error = true)
            funResponse(MoviesList(0, listOf(m), 0))
        }

        override fun onResponse(call: Call<MoviesList?>, response: Response<MoviesList?>) {
            response.body()?.apply(funResponse)
        }
    }


}