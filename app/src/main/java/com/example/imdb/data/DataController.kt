package com.example.imdb.data

import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.Movie
import com.example.imdb.network.WebController

object DataController {

    private lateinit var language: String

    fun setupDatabase(language: String) {
        this.language = language
    }

    fun restartDatabase() {
        DatabaseMovies.drop()
    }

    fun getLanguage() = language

    fun loadLatest(funResponse: () -> Unit) {
        if (getLatest().isEmptyOrInLoading()) {
            addLoadingLatest()
            WebController.loadLatest {
                addLatest(it)
                funResponse()
            }
        }
    }

    fun loadNowPlaying(funResponse: () -> Unit) {
        if (getNowPlaying().isEmptyOrInLoading()) {
            addLoadingNowPlaying()
            WebController.loadNowPlaying {
                addNowPlaying(it.results)
                funResponse()
            }
        }
    }

    fun loadPopular(funResponse: () -> Unit) {
        if (getPopular().isEmptyOrInLoading()) {
            addLoadingPopular()
            WebController.loadPopular {
                addPopular(it.results)
                funResponse()
            }
        }
    }

    fun loadTopRated(funResponse: () -> Unit) {
        if (getTopRated().isEmptyOrInLoading()) {
            addLoadingTopRated()
            WebController.loadTopRated {
                println(it.results)
                addTopRated(it.results)
                funResponse()
            }
        }
    }

    fun loadUpcoming(funResponse: () -> Unit) {
        if (getUpcoming().isEmptyOrInLoading()) {
            addLoadingUpcoming()
            WebController.loadUpcoming {
                addUpcoming(it.results)
                funResponse()
            }
        }
    }

    fun getLatest() = DatabaseMovies.getLatest()

    fun getNowPlaying() = DatabaseMovies.getNowPlaying()

    fun getPopular() = DatabaseMovies.getPopular()

    fun getTopRated() = DatabaseMovies.getTopRated()

    fun getUpcoming() = DatabaseMovies.getUpcoming()

    private fun addLatest(movie: Movie) = DatabaseMovies.addLatest(movie)

    private fun addNowPlaying(movies: List<Movie>) = DatabaseMovies.addNowPlaying(movies)

    private fun addPopular(movies: List<Movie>) = DatabaseMovies.addPopular(movies)

    private fun addTopRated(movies: List<Movie>) = DatabaseMovies.addTopRated(movies)

    private fun addUpcoming(movies: List<Movie>) = DatabaseMovies.addUpcoming(movies)

    private fun addLoadingLatest() = DatabaseMovies.addLoadingLatest()

    private fun addLoadingNowPlaying() = DatabaseMovies.addLoadingNowPlaying()

    private fun addLoadingTopRated() = DatabaseMovies.addLoadingTopRated()

    private fun addLoadingPopular() = DatabaseMovies.addLoadingPopular()

    private fun addLoadingUpcoming() = DatabaseMovies.addLoadingUpcoming()

    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {

        for (movie in this) {
            if (movie.loading) {
                return true
            }
        }

        if (this.count() == 0)
            return true

        return false
    }
}
