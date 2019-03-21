package com.example.imdb.network.themoviedb

import com.example.imdb.data.entity.MoviesList
import com.example.imdb.data.entity.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBAPI {

    @GET("now_playing")
    fun getNowPlaying(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Call<MoviesList>

    @GET("latest")
    fun getLatest(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Call<Movie>

    @GET("popular")
    fun getPopular(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Call<MoviesList>

    @GET("top_rated")
    fun getTopRated(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Call<MoviesList>

    @GET("upcoming")
    fun getUpcoming(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Call<MoviesList>


}