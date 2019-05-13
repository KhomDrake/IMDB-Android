package com.example.imdb.network

import com.example.imdb.network.themoviedb.ITheMovieDBAPI
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TheMovieDBAPI {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var aDefault: ITheMovieDBAPI? = null

    fun service(): ITheMovieDBAPI {
        if (aDefault == null) {
            aDefault = retrofit.create(ITheMovieDBAPI::class.java)
        }

        return aDefault!!
    }

}