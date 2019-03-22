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

    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        val latest = getLatest()
        if (latest.isEmptyOrInLoading()) {
            WebController.loadLatest {
                addLatest(it)
                funResponse(listOf(it))
            }
        } else {
            funResponse(latest)
        }
    }

    fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        val nowPlaying = getNowPlaying()
        if (nowPlaying.isEmptyOrInLoading()) {
            WebController.loadNowPlaying {
                addNowPlaying(it.results)
                funResponse(it.results)
            }
        } else {
            funResponse(nowPlaying)
        }
    }

    fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        val popular = getPopular()
        if (popular.isEmptyOrInLoading()) {
            WebController.loadPopular {
                addPopular(it.results)
                funResponse(it.results)
            }
        } else {
            funResponse(popular)
        }
    }

    fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        val topRated = getTopRated()
        if (topRated.isEmptyOrInLoading()) {
            WebController.loadTopRated {
                addTopRated(it.results)
                funResponse(it.results)
            }
        } else {
            funResponse(topRated)
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

    private fun getLatest() = DatabaseMovies.getLatest()

    private fun getNowPlaying() = DatabaseMovies.getNowPlaying()

    private fun getPopular() = DatabaseMovies.getPopular()

    private fun getTopRated() = DatabaseMovies.getTopRated()

    private fun getUpcoming() = DatabaseMovies.getUpcoming()

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
