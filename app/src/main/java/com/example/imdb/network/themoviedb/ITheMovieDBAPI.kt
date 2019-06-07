package com.example.imdb.network.themoviedb

import com.example.imdb.data.entity.http.*
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.data.entity.http.movie.MovieCredit
import com.example.imdb.data.entity.http.movie.MovieDetail
import com.example.imdb.data.entity.http.movie.MoviesList
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ITheMovieDBAPI {

    // Sessão

    @GET("authentication/guest_session/new")
    fun createGuestSessionAsync(
        @Query("api_key") key: String
    ): Deferred<Session>

    @GET("authentication/token/new")
    fun createRequestTokenAsync(
        @Query("api_key") key: String
    ): Deferred<RequestToken>

    @GET("authentication/session/new")
    fun createSession(
        @Query("api_key") key: String,
        @Body requestToken: String
    ): Deferred<CreateSession>

    @POST("authentication/token/validate_with_login")
    fun createSessionWithLoginAsync(
        @Query("api_key") key: String,
        @Body loginBody: LoginBody
    ): Deferred<LoginResponse>

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

    @GET("movie/{idMovie}")
    fun getDetailMovieAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MovieDetail>

    @GET("movie/{idMovie}/recommendations")
    fun getRecommendationMovieAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<MoviesList>

    @GET("movie/{idMovie}/reviews")
    fun getReviewsMovieAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Reviews>

    @GET("movie/{idMovie}/credits")
    fun getMovieCreditAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String
    ): Deferred<MovieCredit>

    @POST("movie/{idMovie}/rating")
    fun rateMovieGuestAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String,
        @Query("guest_session_id") sessionId: String,
        @Body body: Rate
    ): Deferred<RateResponse>

    @POST("movie/{idMovie}/rating")
    fun rateMovieAsync(
        @Path("idMovie") id: Int,
        @Query("api_key") key: String,
        @Query("session_id") sessionId: String,
        @Body body: Rate
    ): Deferred<RateResponse>

    @GET("guest_session/{guest_session_id}/rated/movies")
    fun getRateMovieGuest(
        @Path("guest_session_id") sessionId: String,
        @Query("api_key") key: String
    )

    // Tv

    @GET("tv/airing_today")
    fun getAiringTodayTVAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/latest")
    fun getLatestTVAsync(
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/on_the_air")
    fun getOnTheAirAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/popular")
    fun getPopularTVAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/top_rated")
    fun getTopRatedTVAsync(
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") p: Int = 1
    ): Deferred<Any>

    @GET("tv/{idTv}")
    fun getTvDetailAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/credits")
    fun getTvCreditAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/recommendations")
    fun getTvRecommendationAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/reviews")
    fun getTvReviewAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/episode_groups")
    fun getTvEpisodeGroupsAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/screened_theatrically")
    fun getTvScreenedThreatricallyAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/similar")
    fun getTvSimilarAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/translations")
    fun getTvTranslationsAsync(
        @Path("idTv") id: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/season/{season}")
    fun getTvSeasonAsync(
        @Path("idTv") id: Int,
        @Path("season") season: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

    @GET("tv/{idTv}/season/{season}/episode/{episode}")
    fun getTvSeasonEpisodeAsync(
        @Path("idTv") id: Int,
        @Path("season") season: Int,
        @Path("episode") episode: Int,
        @Query("api_key") key: String,
        @Query("language") language: String
    ): Deferred<Any>

}