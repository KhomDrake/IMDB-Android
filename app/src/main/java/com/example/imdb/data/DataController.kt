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

    fun loadLatest(funResponse: (movies: MutableList<Movie>) -> Unit) {
        if (getLatest().isEmptyOrInLoading()) {
            WebController.loadLatest {
                addLatest(it)
                funResponse(getLatest())
            }
        }
    }

    fun loadNowPlaying(funResponse: (movies: MutableList<Movie>) -> Unit) {
        if (getNowPlaying().isEmptyOrInLoading()) {
            WebController.loadNowPlaying {
                addNowPlaying(it.results)
                funResponse(getNowPlaying())
            }
        }
    }

    fun loadPopular(funResponse: (movies: MutableList<Movie>) -> Unit) {
        if (getPopular().isEmptyOrInLoading()) {
            WebController.loadPopular {
                addPopular(it.results)
                funResponse(getPopular())
            }
        }
    }

    fun loadTopRated(funResponse: (movies: MutableList<Movie>) -> Unit) {
        if (getTopRated().isEmptyOrInLoading()) {
            WebController.loadTopRated {
                addTopRated(it.results)
                funResponse(getTopRated())
            }
        }
    }

    fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        val upcoming = getUpcoming()
        if (upcoming.isEmptyOrInLoading()) {
            WebController.loadUpcoming {
                addUpcoming(it.results)
                funResponse(it.results)
            }
        } else {
            funResponse(upcoming)
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
