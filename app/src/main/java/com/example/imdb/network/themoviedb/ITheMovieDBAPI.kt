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

    @GET("movie/{idReview}")
    fun getDetailMovie(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MovieDetail>

    @GET("movie/{idReview}/recommendations")
    fun getRecommendationMovie(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MoviesList>

    @GET("movie/{idReview}/reviews")
    fun getReviewsMovie(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Reviews>

    @GET("movie/{idReview}/credits")
    fun getMovieCredit(
        @Path("idReview") id: Int,
        @Query("api_key") key: String
    ): Deferred<MovieCredit>

    // 89386

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

    @GET("tv/top_rated")
    fun getTopRatedTV(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/{idReview}")
    fun getTvDetail(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/credits")
    fun getTvCredit(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/recommendations")
    fun getTvRecommendation(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/reviews")
    fun getTvReview(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/episode_groups")
    fun getTvEpisodeGroups(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/screened_theatrically")
    fun getTvScreenedThreatrically(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/similar")
    fun getTvSimilar(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/translations")
    fun getTvTranslations(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/season/{season}")
    fun getTvSeason(
        @Path("idReview") id: Int,
        @Path("season") season: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

    @GET("tv/{idReview}/season/{season}/episode/{episode}")
    fun getTvSeasonEpisode(
        @Path("idReview") id: Int,
        @Path("season") season: Int,
        @Path("episode") episode: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : Deferred<Any>

}