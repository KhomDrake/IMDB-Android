package com.example.imdb.data.database

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imdb.data.entity.http.*
import com.example.imdb.data.entity.table.*

@Database(entities = arrayOf(TableMovie::class,
                            TableMovieCategory::class,
                            TableMovieDetail::class,
                            TableMoviesList::class,
                            TableRecommendation::class,
                            TableReview::class,
                            TableReviewInformation::class), version = 1)
abstract class DatabaseMovies : RoomDatabase() {

    abstract fun moviesDao(): DaoMovies

    private var movieDetail: MovieDetail =
        MovieDetail(false, 0, "", "", "", 0, "", 0.0, 0)

    private var recommendationLastMovieDetail: Recommendation =
        Recommendation(
            -1,
            MoviesList(0, listOf(), 0)
        )
    private var reviewsLastMovieDetail: Reviews =
        Reviews(-1, 0, listOf(), 0, 0)

    fun getLatest() : MutableList<Movie> = mutableListOf()

    fun getNowPlaying() : MutableList<Movie> = mutableListOf()

    fun getPopular() : MutableList<Movie> = mutableListOf()

    fun getTopRated() : MutableList<Movie> = mutableListOf()

    fun getUpcoming() : MutableList<Movie> = mutableListOf()

    fun getDetailMovie() : MovieDetail = movieDetail

    fun getRecommendationLastMovie() : Recommendation = recommendationLastMovieDetail

    fun getReviewsLastMovieDetail() : Reviews = reviewsLastMovieDetail

    private fun getListMovies(type: String): MutableList<Movie> {
        val movies = mutableListOf<Movie>()

//        if (sharedPreferences.all[type] == null) return movies
//
//        val ids = (sharedPreferences.all[type] as String).split(";")
//
//        for (id in ids) {
//            if (id != "") {
//                movies.add(movies.count(), getMovie(type, id.trim()))
//            }
//        }

        return movies
    }

    private fun setList(type: String, movies: List<Movie>) {
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        var ids = ""
//        for (movie in movies) {
//            mountMovie(editor, type, movie)
//            ids += "${movie.id};"
//        }
//        editor.putString(type, ids)
//        editor.apply()
    }

    private fun mountMovie(editor: SharedPreferences.Editor, type: String, movie: Movie) {
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
        val originalTitle = "" //sharedPreferences.all["$type${id}originalTitle"].toString()
        val posterPath = "" //sharedPreferences.all["$type${id}posterPath"].toString()

        return Movie(id.toInt(), originalTitle, posterPath, "", false, false)
    }

    fun setReviews(reviews: Reviews) {
        reviewsLastMovieDetail = reviews
    }

    fun setRecommendationLastMovie(recommendation: Recommendation) {
        recommendationLastMovieDetail = recommendation
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        this.movieDetail = movieDetail
    }

    fun setLatest(movie: Movie) {
//        if(movie.error)
//            setList(latest, listOf())
//        else
//            setList(latest, listOf(movie))
    }

    fun setNowPlaying(movies: List<Movie>) {
//        setList(nowPlaying, movies)
    }

    fun setPopular(movies: List<Movie>) {
//        setList(popular, movies)
    }

    fun setTopRated(movies: List<Movie>) {
//        setList(topRated, movies)
    }

    fun setUpcoming(movies: List<Movie>) {
//        setList(upcoming, movies)
    }

    companion object {

        var Instance: DatabaseMovies? = null

        fun instance(ctx: Context) {
            if(Instance != null)
                return

            synchronized(DatabaseMovies::class) {
                Instance = Room.databaseBuilder(
                    ctx.applicationContext, DatabaseMovies::class.java,
                    "movie.db"
                ).allowMainThreadQueries().build()
            }
        }
    }
}