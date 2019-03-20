package com.example.imdb.data

import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Result
import com.example.imdb.network.WebControllerTheMovieDB

object DataController {

    private lateinit var language: String

    fun setupDatabase(language: String) {
        Database.addLoadingAll(Result("", "", "", true))
        this.language = language
    }

    fun getLanguage() = language

    fun loadLatest(funResponse: (body: Result) -> Unit) {
        WebControllerTheMovieDB.getLatest(funResponse)
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.getNowPlaying(funResponse)
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.getPopular(funResponse)
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.getTopRated(funResponse)
    }

    fun loadUpcoming(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.getUpcoming(funResponse)
    }


    fun getLatest() = Database.getLatest()

    fun getNowPlaying() = Database.getNowPlaying()

    fun getPopular() = Database.getPopular()

    fun getTopRated() = Database.getTopRated()

    fun getUpcoming() = Database.getUpcoming()

    fun addLatest(movie: Result) = Database.addLatest(movie)

    fun addNowPlaying(movies: List<Result>) = Database.addNowPlaying(movies)

    fun addPopular(movies: List<Result>) = Database.addPopular(movies)

    fun addTopRated(movies: List<Result>) = Database.addTopRated(movies)

    fun addUpcoming(movies: List<Result>) = Database.addUpcoming(movies)


}