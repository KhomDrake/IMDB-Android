package com.example.imdb.data.database

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.imdb.ctx
import com.example.imdb.data.entity.*

object DatabaseMovies {

    private val sharedPreferences
        get() = ctx.getSharedPreferences("test", MODE_PRIVATE)

    private const val latest = "latest"
    private const val nowPlaying = "nowPlaying"
    private const val popular = "popular"
    private const val topRated = "topRated"
    private const val upcoming = "upcoming"
    private var dataLastMovieDetail: MovieDetail = MovieDetail(false, "", 0, listOf(),
        "", -1, "", "", 0.0, "", listOf(), "",
        0, 0, listOf(), "", "", "",  0.0, 0)

    private var recommendationLastMovieDetail: Recommendation = Recommendation(-1, MoviesList(0, listOf(), 0))
    private var reviewsLastMovieDetail: Reviews = Reviews(-1, 0, listOf(), 0, 0)

    fun getLatest() : MutableList<Movie> = getListMovies(latest)

    fun getNowPlaying() : MutableList<Movie> = getListMovies(nowPlaying)

    fun getPopular() : MutableList<Movie> = getListMovies(popular)

    fun getTopRated() : MutableList<Movie> = getListMovies(topRated)

    fun getUpcoming() : MutableList<Movie> = getListMovies(upcoming)

    fun getDetailMovie() : MovieDetail = dataLastMovieDetail

    fun getRecommendationLastMovie() : Recommendation = recommendationLastMovieDetail

    fun getReviewsLastMovieDetail() : Reviews = reviewsLastMovieDetail

    fun setMovieDetail(movieDetail: MovieDetail) {
        dataLastMovieDetail = movieDetail
    }

    private fun getListMovies(type: String): MutableList<Movie> {
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

    private fun setList(type: String, movies: List<Movie>) {
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
        val keyOriginalTitle = "$type${movie.id}originalTitle"
        val keyPosterPath = "$type${movie.id}posterPath"

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
        val originalTitle = sharedPreferences.all["$type${id}originalTitle"].toString()
        val posterPath = sharedPreferences.all["$type${id}posterPath"].toString()

        return Movie(id.toInt(), originalTitle, posterPath, "", false, false)
    }

    fun setLatest(movie: Movie) {
        if(movie.error)
            setList(latest, listOf())
        else
            setList(latest, listOf(movie))
    }

    fun setNowPlaying(movies: List<Movie>) {
        setList(nowPlaying, movies)
    }

    fun setPopular(movies: List<Movie>) {
        setList(popular, movies)
    }

    fun setTopRated(movies: List<Movie>) {
        setList(topRated, movies)
    }

    fun setUpcoming(movies: List<Movie>) {
        setList(upcoming, movies)
    }
}