package com.example.imdb.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imdb.MovieCategory
import com.example.imdb.data.entity.http.*
import com.example.imdb.data.entity.table.*
@Database(entities = arrayOf(TableMovie::class,
                            TableMovieCategory::class,
                            TableMovieDetail::class,
                            TableMoviesList::class,
                            TableReview::class,
                            TableLastUpdateCategory::class,
                            TableCast::class,
                            TableMovieRecommendation::class,
                            TableReviewInformation::class), version = 1)
abstract class DatabaseMovies : RoomDatabase() {

    abstract fun moviesDao(): DaoMovies

    fun setup() {
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.Latest.ordinal, 1, 1))
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.NowPlaying.ordinal, 1, 1))
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.Popular.ordinal, 1, 1))
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.TopRated.ordinal, 1, 1))
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.Upcoming.ordinal, 1, 1))
        moviesDao().insertMovieList(TableMoviesList(MovieCategory.Recommendation.ordinal, 1, 1))
    }

    fun getMovieCredit(idMovie: Int) : MovieCredit {
        val creditMovieDb = moviesDao().getMovieCreditCast(idMovie)
        val id= if(creditMovieDb.isEmpty()) 0 else idMovie
        val creditMovie = mutableListOf<Cast>()
        for (cast in creditMovieDb) {
            creditMovie.add(Cast(cast.idCast, cast.character, cast.gender, cast.id, cast.name, cast.order, cast.profilePath))
        }
        return MovieCredit(creditMovie, id, false)
    }

    fun getLatest() : MutableList<Movie> = getMovies(MovieCategory.Latest)

    fun getNowPlaying() : MutableList<Movie> = getMovies(MovieCategory.NowPlaying)

    fun getPopular() : MutableList<Movie> = getMovies(MovieCategory.Popular)

    fun getTopRated() : MutableList<Movie> = getMovies(MovieCategory.TopRated)

    fun getUpcoming() : MutableList<Movie> = getMovies(MovieCategory.Upcoming)

    fun getDetailMovie(idMovie: Int) : MovieDetail {
        val movieDetailDb = moviesDao().getMovieDetail(idMovie)
        if(movieDetailDb != null) return MovieDetail(
            movieDetailDb.adult,
            movieDetailDb.idMovieDetail,
            movieDetailDb.overview,
            movieDetailDb.posterPath,
            movieDetailDb.releaseDate,
            movieDetailDb.runtime,
            movieDetailDb.title,
            movieDetailDb.voteAverage,
            movieDetailDb.voteCount,
            false
        )
        else {
            return MovieDetail(false, 0, "", "", "", 0, "", 0.0, 0, false)
        }
    }

    fun getRecommendationLastMovie(idMovie: Int) : Recommendation {
        val moviesDb = moviesDao().getRecommendationMovie(idMovie)
        val movies = tableMoviesToMovies(moviesDb)
        if(movies.isEmpty())
            return Recommendation(0, MoviesList(0, movies, 0))
        else
            return Recommendation(idMovie, MoviesList(0, movies, 0))
    }

    fun getMovieReviews(idMovie: Int) : Reviews {
        val reviews = moviesDao().getMovieReviews(idMovie)

        var id = -1
        var page = 0
        val listOfReviews = mutableListOf<Review>()
        var totalPages = 0
        var totalResults = 0
        var idMovie = 0

        if(reviews.isNotEmpty()) {
            val reviewInformation = reviews[0]
            id = reviewInformation.reviewInformation.idReviewInformation
            page = reviewInformation.reviewInformation.page

            for (review in reviews) {
                val r = review.review
                listOfReviews.add(Review(r.author, r.content, r.idReview.toString(), r.url))
            }
            totalPages = reviewInformation.reviewInformation.totalPages
            totalResults = reviewInformation.reviewInformation.totalResults
            idMovie = reviewInformation.reviewInformation.idMovie_fk
        }

        return Reviews(id, page, listOfReviews.toList(), totalPages, totalResults, idMovie, false)
    }

    fun setReviews(reviews: Reviews) {
        moviesDao().insertReviewInformation(TableReviewInformation(reviews.id, reviews.idMovie, 1, 1, 1))
        for(review in reviews.results) {
            moviesDao().insertReview(TableReview(0, review.author, review.content, review.url, reviews.id))
        }
    }

    fun setRecommendationLastMovie(recommendation: Recommendation) {
        val idMovie = recommendation.id
        for (movie in recommendation.moviesList.results) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
            moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + movie.id, idMovie, movie.id))
        }
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id, movieDetail.overview, movieDetail.posterPath.toString(), movieDetail.releaseDate, movieDetail.runtime, movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
    }

    fun setLatest(movie: Movie) {
        moviesDao().deleteMovieCategory(MovieCategory.Latest.ordinal)
        moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
        moviesDao().insertMovieCategory(TableMovieCategory(0, movie.id, MovieCategory.Latest.ordinal))
    }

    fun setNowPlaying(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.NowPlaying.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
            moviesDao().insertMovieCategory(TableMovieCategory(0, movie.id, MovieCategory.NowPlaying.ordinal))
        }
    }

    fun setPopular(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.Popular.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
            moviesDao().insertMovieCategory(TableMovieCategory(0, movie.id, MovieCategory.Popular.ordinal))
        }
    }

    fun setTopRated(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.TopRated.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
            moviesDao().insertMovieCategory(TableMovieCategory(0, movie.id, MovieCategory.TopRated.ordinal))
        }
    }

    fun setUpcoming(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.Upcoming.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title))
            moviesDao().insertMovieCategory(TableMovieCategory(0, movie.id, MovieCategory.Upcoming.ordinal))
        }
    }

    fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        Log.i("vini", credit.toString())
        for (cast in credit.cast) {
            moviesDao().insertMovieCast(TableCast(cast.castId, idMovie, cast.character, cast.gender, cast.id, cast.name, cast.order, cast.profilePath))
        }
    }

    fun lastTimeUpdateCategory(movieCategory: MovieCategory, currentTime: Long) {
        moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(movieCategory.ordinal, currentTime))
    }

    private fun getMovies(movieCategory: MovieCategory) : MutableList<Movie> {
        val moviesDb = moviesDao().getMoviesListAndMovie(movieCategory.ordinal)
        return tableMoviesToMovies(moviesDb)
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for (movie in moviesDb) {
            movies.add(Movie(movie.idMovie, movie.originalTitle, movie.posterPath, movie.title, false, false))
        }
        return movies
    }

    fun getLastTimeUpdateCategory(movieCategory: MovieCategory): Long {
        val last = moviesDao().getLastUpdateCategory(movieCategory.ordinal)
        return if(last != null) last.timeUpdate else System.currentTimeMillis()
    }

    companion object {

        var Instance: DatabaseMovies? = null

        fun instance(ctx: Context) {
            if(Instance != null) return

            synchronized(DatabaseMovies::class) {
                Instance = Room.databaseBuilder(
                    ctx.applicationContext, DatabaseMovies::class.java,
                    "movie.db"
                ).allowMainThreadQueries().build()
            }
        }
    }
}