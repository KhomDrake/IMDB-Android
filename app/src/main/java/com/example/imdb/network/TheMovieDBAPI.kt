package com.example.imdb.network

import com.example.imdb.entity.NowPlaying.NowPlaying
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

}