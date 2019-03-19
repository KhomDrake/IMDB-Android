package com.example.imdb.network

import com.example.imdb.entity.Latest
import com.example.imdb.entity.NowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBAPI {

    @GET("now_playing")
    fun getNowPlaying(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p : Int = 1
    ): Call<NowPlaying>

    @GET("latest")
    fun getLatest(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Call<Latest>

    @GET("popular")
    fun getPopular(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p : Int = 1
    ): Call<NowPlaying>

    @GET("top_rated")
    fun getTopRated(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p : Int = 1
    ): Call<NowPlaying>

    @GET("upcoming")
    fun getUpcoming(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p : Int = 1
    ): Call<NowPlaying>


}