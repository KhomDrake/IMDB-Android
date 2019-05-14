package com.example.imdb.network.themoviedb

import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ITheMovieDBAPI {


    @GET("movie/now_playing")
     fun getNowPlayingMovie(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/latest")
    fun getLatestMovie(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Movie>

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/top_rated")
    fun getTopRatedMovie(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/upcoming")
    fun getUpcomingMovie(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/{id}")
    fun getDetailMovie(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MovieDetail>

    @GET("movie/{id}/recommendations")
    fun getRecommendationMovie(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MoviesList>

    @GET("movie/{id}/reviews")
    fun getReviewsMovie(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Reviews>

    @GET("movie/{id}/credits")
    fun getMovieCredit(
        @Path("id") id: Int,
        @Query("api_key") key: String
    ): Deferred<MovieCredit>


    // 89386

    @GET("tv/{id}")
    fun getTvDetail(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{id}/credits")
    fun getTvCredit(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{id}/recommendations")
    fun getTvRecommendation(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{id}/reviews")
    fun getTvReview(
        @Path("id") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/airing_today")
    fun getAiringTodayTV(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/latest")
    fun getLatestTV(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/on_the_air")
    fun getOnTheAir(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/popular")
    fun getPopularTV(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/upcoming")
    fun getTopRatedTV(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>





}