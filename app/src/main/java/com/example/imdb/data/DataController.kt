package com.example.imdb.data

import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Movie
import com.example.imdb.network.WebControllerTheMovieDB

object DataController {

    private lateinit var language: String

    fun setupDatabase(language: String) {
        Database.addLoadingAll(Movie("", "", "", true))
        this.language = language
    }

    fun getLanguage() = language

    fun loadLatest(funResponse: (body: Movie) -> Unit) {
        WebControllerTheMovieDB.loadLatest(funResponse)
    }

    fun loadNowPlaying(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadNowPlaying(funResponse)
    }

    fun loadPopular(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadPopular(funResponse)
    }

    fun loadTopRated(funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadTopRated(funResponse)
    }

    fun loadUpcoming(funResponse: (body: List<Movie>) -> Unit) {
        if(Database.getUpcoming().isNotEmpty()){
            funResponse.invoke(getUpcoming())
        } else {
            WebControllerTheMovieDB.loadUpcoming{
                addUpcoming(it.results)
                funResponse.invoke(getUpcoming())
            }
        }
    }


    fun loadNowPlaying(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadNowPlaying(pag, funResponse)
    }

    fun loadPopular(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadPopular(pag, funResponse)
    }

    fun loadTopRated(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadTopRated(pag, funResponse)
    }

    fun loadUpcoming(pag: Int, funResponse: (body: MoviesList) -> Unit) {
        WebControllerTheMovieDB.loadUpcoming(pag, funResponse)
    }

    fun getLatest(): MutableList<Movie> = Database.getLatest()

    fun getNowPlaying() = Database.getNowPlaying()

    fun getPopular() = Database.getPopular()

    fun getTopRated() = Database.getTopRated()

    fun getUpcoming() = Database.getUpcoming()

    fun addLatest(movie: Movie) = Database.addLatest(movie)

    fun addNowPlaying(movies: List<Movie>) = Database.addNowPlaying(movies)

    fun addPopular(movies: List<Movie>) = Database.addPopular(movies)

    fun addTopRated(movies: List<Movie>) = Database.addTopRated(movies)

    fun addUpcoming(movies: List<Movie>) = Database.addUpcoming(movies)

    fun addLoadingNowPlaying() = Database.addLoadingNowPlaying()

    fun addLoadingTopRated() = Database.addLoadingTopRated()

    fun addLoadingPopular() = Database.addLoadingPopular()

    fun addLoadingUpcoming() = Database.addLoadingUpcoming()

}