package com.example.imdb.data.database

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.imdb.ctx
import com.example.imdb.data.entity.Movie

object DatabaseMovies {

    private val sharedPreferences
        get() = ctx.getSharedPreferences("test", MODE_PRIVATE)

    private val latest = "latest"
    private val nowPlaying = "nowPlaying"
    private val popular = "popular"
    private val topRated = "topRated"
    private val upcoming = "upcoming"


    fun getLatest() = makeListMovies(latest)

    fun getNowPlaying() = makeListMovies(nowPlaying)

    fun getPopular() = makeListMovies(popular)

    fun getTopRated() = makeListMovies(topRated)

    fun getUpcoming() = makeListMovies(upcoming)

    fun drop() {

    }

    private fun clean(list: MutableList<Movie>) {
        list.apply {
            removeAll(this)
        }
    }

    private fun makeListMovies(type: String): MutableList<Movie> {
        val movies = mutableListOf<Movie>()

        if (sharedPreferences.all[type] == null) return movies

        val ids = (sharedPreferences.all[type] as String).split(";")

        for (id in ids) {
            if (id != "") {
                movies.add(movies.count(), getMovie(type, id.trim()))
            }
        }

        return movies
    }

    private fun addList(type: String, movies: List<Movie>) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        var ids = ""
        for (movie in movies) {
            createMovie(editor, type, movie)
            ids += "${movie.id};"
        }
        editor.putString(type, ids)
        editor.apply()
    }

    private fun createMovie(editor: SharedPreferences.Editor, type: String, movie: Movie) {
        val keyOriginalTitle = "${type}${movie.id}originalTitle"
        val keyPosterPath = "${type}${movie.id}posterPath"

        var value = movie.posterPath

        if (movie.posterPath == "" || movie.posterPath == "null" || movie.posterPath == null) {
            value = ""
        }

        editor.run {
            putString(keyOriginalTitle, movie.originalTitle)
            putString(keyPosterPath, value)
        }
    }

    private fun getMovie(type: String, id: String): Movie {
        val originalTitle = sharedPreferences.all["${type}${id}originalTitle"].toString()
        val posterPath = sharedPreferences.all["${type}${id}posterPath"].toString()

        return Movie(id.toInt(), originalTitle, posterPath, "", false)
    }

    fun addLatest(movie: Movie) {
        addList(latest, listOf(movie))
    }

    fun addNowPlaying(movies: List<Movie>) {
        addList(nowPlaying, movies)
    }

    fun addPopular(movies: List<Movie>) {
        addList(popular, movies)
    }

    fun addTopRated(movies: List<Movie>) {
        addList(topRated, movies)
    }

    fun addUpcoming(movies: List<Movie>) {
        addList(upcoming, movies)
    }
}