package com.example.imdb.data.database

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.TAG_VINI
import com.example.imdb.data.entity.http.Cast
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.data.entity.table.*
import com.example.imdb.ui.*
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
        val creditMovie = mutableListOf<Cast>()
        for (cast in creditMovieDb) {
            creditMovie.add(
                Cast(
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

    fun getCategory(theMovieDbCategory: TheMovieDbCategory) = getMovies(theMovieDbCategory)

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
        } else {
            EMPTY_MOVIE_DETAIL
        }
    }

    fun getRecommendationLastMovie(idMovie: Int) : Recommendation {
        val moviesDb = moviesDao().getRecommendationMovie(idMovie)
        val movies = tableMoviesToMovies(moviesDb)
        return if(movies.isEmpty())
                Recommendation(
                    ZERO, MoviesList(
                        ZERO, movies,
                        ZERO
                    ))
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

    fun getLastTimeUpdateCategory(theMovieDbCategory: TheMovieDbCategory) : Long {
        val last = moviesDao().getLastUpdateCategory(theMovieDbCategory.ordinal)
        return last?.timeUpdate ?: System.currentTimeMillis()
    }

    fun getFavorites() : List<Movie> {
        val favoritesDb = moviesDao().getFavorite()
        return tableMoviesToMovies(favoritesDb)
    }

    fun setReviews(reviews: Reviews) {
        if(reviews.reviews.isNullOrEmpty() || reviews.reviews.first().error) return

        moviesDao().insertReviewInformation(TableReviewInformation(reviews.idReview, reviews.idReview, PAGE_ONE, PAGE_ONE, ONE))
        reviews.reviews.forEach {
            moviesDao().insertReview(TableReview(0, it.author, it.content, it.url, reviews.idReview))
        }
    }

    fun setRecommendationMovie(recommendation: Recommendation) {
        if(recommendation.moviesList.results.isNullOrEmpty() || recommendation.moviesList.results.first().error) return

        val idMovie = recommendation.id
        recommendation.moviesList.results.forEach {
            moviesDao().insertMovie(TableMovie(it.id, it.originalTitle, it.posterPath, it.title, it.adult))
            moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + it.id, idMovie, it.id))
        }
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        if(movieDetail.error) return

        moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id,
            movieDetail.overview, movieDetail.posterPath.toString(), movieDetail.releaseDate, movieDetail.runtime,
            movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
    }

    fun setMovie(movies: List<Movie>, theMovieDbCategory: TheMovieDbCategory) {
        if(movies.isNullOrEmpty() || movies.first().error) return

        moviesDao().deleteMovieCategory(theMovieDbCategory.ordinal)
        movies.forEach {
            val movie = it
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, theMovieDbCategory.ordinal))
        }
    }

    fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        if(credit.cast.isNullOrEmpty() || credit.cast.first().error) return

        credit.cast.forEach {
            moviesDao().insertMovieCast(TableCast(it.castId, idMovie, it.character, it.gender,
                it.id, it.name, it.order, it.profilePath))
        }
    }

    fun lastTimeUpdateCategory(theMovieDbCategory: TheMovieDbCategory, currentTime: Long) {
        moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(theMovieDbCategory.ordinal, currentTime))
    }

    fun favoriteMovie(movieId: Int, toFavorite: Boolean) =
        if(toFavorite) moviesDao().insertFavorite(TableFavorite(movieId)) else moviesDao().deleteFavorite(movieId)

    open fun setup() {
        val listOfMovieDbCategory = listOf(TheMovieDbCategory.MovieLatest.ordinal,
            TheMovieDbCategory.MovieNowPlaying.ordinal,
            TheMovieDbCategory.MoviePopular.ordinal,
            TheMovieDbCategory.MovieTopRated.ordinal,
            TheMovieDbCategory.MovieUpcoming.ordinal,
            TheMovieDbCategory.MovieRecommendation.ordinal)

        coroutine {
            listOfMovieDbCategory.forEach { moviesDao().insertMovieList(TableMoviesList(it, 1, 1)) }
        }
    }

    var s = 1
    private fun getMovies(theMovieDbCategory: TheMovieDbCategory) : List<Movie> {
        val moviesDb = moviesDao().getMoviesListAndMovie(theMovieDbCategory.ordinal)
        return tableMoviesToMovies(moviesDb)
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : List<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for (movie: TableMovie in moviesDb) {
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
        mutableListOf<Movie>().toList()
        return movies.toList()
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.Default) {
            block()
        }
    }
}