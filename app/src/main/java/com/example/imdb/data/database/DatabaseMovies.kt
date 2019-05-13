package com.example.imdb.data.database

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.EMPTY_MOVIE_DETAIL
import com.example.imdb.TAG_VINI
import com.example.imdb.ZERO
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.data.entity.table.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [
    TableMovie::class,
    TableMovieCategory::class,
    TableMovieDetail::class,
    TableMoviesList::class,
    TableReview::class,
    TableFavorite::class,
    TableLastUpdateCategory::class,
    TableCast::class,
    TableMovieRecommendation::class,
    TableReviewInformation::class
], version = 1)
abstract class DatabaseMovies : RoomDatabase() {

    abstract fun moviesDao(): DaoMovies
    private val language = ""

    init {
        setup()
    }

    fun getLanguage() = language

    fun getMovieCredit(idMovie: Int) : MovieCredit {
        val creditMovieDb = moviesDao().getMovieCreditCast(idMovie)
        val id = if(creditMovieDb.isEmpty()) 0 else idMovie
        val creditMovie = mutableListOf<CastMovie>()
        for (cast in creditMovieDb) {
            creditMovie.add(
                CastMovie(
                    cast.idCast,
                    cast.character,
                    cast.gender,
                    cast.id,
                    cast.name,
                    cast.order,
                    cast.profilePath,
                    error = false
                )
            )
        }
        return MovieCredit(creditMovie, id)
    }

    fun getCategory(movieDbCategory: MovieDbCategory) = getMovies(movieDbCategory)

    fun getDetailMovie(idMovie: Int) : MovieDetail {
        val movieDetailDb = moviesDao().getMovieDetail(idMovie)
        return if(movieDetailDb != null) {
            MovieDetail(
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
        } else { EMPTY_MOVIE_DETAIL }
    }

    fun getRecommendationLastMovie(idMovie: Int) : Recommendation {
        val moviesDb = moviesDao().getRecommendationMovie(idMovie)
        val movies = tableMoviesToMovies(moviesDb)
        return if(movies.isEmpty())
                Recommendation(ZERO, MoviesList(ZERO, movies, ZERO))
            else
                Recommendation(idMovie, MoviesList(ZERO, movies, ZERO))
    }

    fun getMovieReviews(_idMovie: Int) : Reviews {
        val reviews = moviesDao().getMovieReviews(_idMovie)

        var id = ZERO
        var page = ZERO
        val listOfReviews = mutableListOf<Review>()
        var totalPages = ZERO
        var totalResults = ZERO
        var idMovie = ZERO

        if(reviews.isNotEmpty()) {
            val reviewInformation = reviews[0]
            id = reviewInformation.reviewInformation.idReviewInformation
            page = reviewInformation.reviewInformation.page

            reviews.forEach {
                val review = it.review
                listOfReviews.add(
                    Review(
                        review.author,
                        review.content,
                        review.idReview.toString(),
                        review.url,
                        false
                    )
                )
            }
            totalPages = reviewInformation.reviewInformation.totalPages
            totalResults = reviewInformation.reviewInformation.totalResults
            idMovie = reviewInformation.reviewInformation.idMovie_fk
        }
        return Reviews(id, page, listOfReviews.toList(), totalPages, totalResults, idMovie)
    }

    fun getLastTimeUpdateCategory(movieDbCategory: MovieDbCategory) : Long {
        val last = moviesDao().getLastUpdateCategory(movieDbCategory.ordinal)
        return last?.timeUpdate ?: System.currentTimeMillis()
    }

    fun getFavorites() : List<Movie> {
        val favoritesDb = moviesDao().getFavorite()
        return tableMoviesToMovies(favoritesDb)
    }

    fun setReviews(reviews: Reviews) {
        Log.i(TAG_VINI, reviews.id.toString())
        Log.i(TAG_VINI, reviews.idMovie.toString())
        moviesDao().insertReviewInformation(TableReviewInformation(reviews.id, reviews.id, 1, 1, 1))
        for(review in reviews.results) {
            moviesDao().insertReview(TableReview(0, review.author, review.content, review.url, reviews.id))
        }
    }

    fun setRecommendationMovie(recommendation: Recommendation) {
        if(recommendation.moviesList.results.isNotEmpty() && recommendation.moviesList.results.first().error) return

        val idMovie = recommendation.id
        for (movie in recommendation.moviesList.results) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
            moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + movie.id, idMovie, movie.id))
        }
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        if(movieDetail.error) return

        moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id,
            movieDetail.overview, movieDetail.posterPath.toString(), movieDetail.releaseDate, movieDetail.runtime,
            movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
    }

    fun setMovie(movies: List<Movie>, movieDbCategory: MovieDbCategory) {
        if(movies.first().error) return

        moviesDao().deleteMovieCategory(movieDbCategory.ordinal)
        movies.forEach {
            val movie = it
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, movieDbCategory.ordinal))
        }
    }

    fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        if(credit.castMovie.isNotEmpty() && credit.castMovie.first().error) return

        for (cast in credit.castMovie) {
            moviesDao().insertMovieCast(TableCast(cast.castId, idMovie, cast.character, cast.gender,
                cast.id, cast.name, cast.order, cast.profilePath))
        }
    }

    fun lastTimeUpdateCategory(movieDbCategory: MovieDbCategory, currentTime: Long) {
        moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(movieDbCategory.ordinal, currentTime))
    }

    fun favoriteMovie(movieId: Int, toFavorite: Boolean) {
        if(toFavorite)
            moviesDao().insertFavorite(TableFavorite(movieId))
        else
            moviesDao().deleteFavorite(movieId)
    }

    private fun setup() {
        val listOfMovieDbCategory = listOf(MovieDbCategory.MovieLatest.ordinal,
            MovieDbCategory.MovieNowPlaying.ordinal,
            MovieDbCategory.MoviePopular.ordinal,
            MovieDbCategory.MovieTopRated.ordinal,
            MovieDbCategory.MovieUpcoming.ordinal,
            MovieDbCategory.MovieRecommendation.ordinal)

        coroutine {
            listOfMovieDbCategory.forEach {
                moviesDao().insertMovieList(TableMoviesList(it, 1, 1))
            }
        }
    }

    private fun getMovies(movieDbCategory: MovieDbCategory) : List<Movie> {
        val moviesDb = moviesDao().getMoviesListAndMovie(movieDbCategory.ordinal)
        return tableMoviesToMovies(moviesDb)
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : List<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for (movie in moviesDb) {
            movies.add(
                Movie(
                    movie.idMovie,
                    movie.originalTitle,
                    movie.posterPath,
                    movie.title,
                    false,
                    false,
                    movie.adult,
                    false
                )
            )
        }
        return movies.toList()
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            block()
        }
    }
}