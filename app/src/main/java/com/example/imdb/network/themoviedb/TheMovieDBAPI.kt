package com.example.imdb.network.themoviedb

import com.example.imdb.data.entity.http.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBAPI {

    @GET("now_playing")
    fun getNowPlaying(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("latest")
    fun getLatest(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Movie>

    @GET("popular")
    fun getPopular(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("top_rated")
    fun getTopRated(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("upcoming")
    fun getUpcoming(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("{id}")
    fun getDetail(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MovieDetail>

    @GET("{id}/recommendations")
    fun getRecommendation(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MoviesList>

    @GET("{id}/reviews")
    fun getReviews(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Reviews>

    @GET("{id}/credits")
    fun getMovieCredit(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): Deferred<MovieCredit>

}