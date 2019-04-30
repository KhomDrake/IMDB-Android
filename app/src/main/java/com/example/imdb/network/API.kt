package com.example.imdb.network

import com.example.imdb.network.themoviedb.TheMovieDBAPI
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/movie/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var default: TheMovieDBAPI? = null

    fun service(): TheMovieDBAPI {
        if (default == null) {
            default = retrofit.create(TheMovieDBAPI::class.java)
        }

        return default!!
    }

}