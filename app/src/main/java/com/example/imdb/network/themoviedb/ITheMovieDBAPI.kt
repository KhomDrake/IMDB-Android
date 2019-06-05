package com.example.imdb.network.themoviedb

import com.example.imdb.data.entity.http.LoginBody
import com.example.imdb.data.entity.http.LoginResponse
import com.example.imdb.data.entity.http.RequestToken
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.Session
import com.example.imdb.data.entity.http.movie.*
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ITheMovieDBAPI {

    // Sessão

    @GET("/authentication/guest_session/new")
    fun createGuestSession(
        @Query("api_key") key: String
    ) : Deferred<Session>

    @GET("/authentication/token/new")
    fun createRequestToken(
        @Query("api_key") key: String
    ) : Deferred<RequestToken>

    @POST("/authentication/token/validate_with_login")
    fun createSessionWithLogin(
        @Body loginBody: LoginBody
    ) : Deferred<LoginResponse>

    // Filmes

    @GET("movie/now_playing")
    fun getNowPlayingMovieAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/latest")
    fun getLatestMovieAsync(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Movie>

    @GET("movie/popular")
    fun getPopularMovieAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/top_rated")
    fun getTopRatedMovieAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/upcoming")
    fun getUpcomingMovieAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<MoviesList>

    @GET("movie/{idReview}")
    fun getDetailMovieAsync(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MovieDetail>

    @GET("movie/{idReview}/recommendations")
    fun getRecommendationMovieAsync(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MoviesList>

    @GET("movie/{idReview}/reviews")
    fun getReviewsMovieAsync(
        @Path("idReview") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Reviews>

    @GET("movie/{idReview}/credits")
    fun getMovieCreditAsync(
        @Path("idReview") id: Int,
        @Query("api_key") key: String
    ): Deferred<MovieCredit>

    // Tv

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