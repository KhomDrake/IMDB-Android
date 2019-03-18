package com.example.imdb.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {

    private val retrofit: Retrofit = Retrofit.Builder()
       .baseUrl("https://api.themoviedb.org/3/movie/")
       .addConverterFactory(GsonConverterFactory.create())
       .build()

    fun service() = retrofit.create(TheMovieDBAPI::class.java)

}