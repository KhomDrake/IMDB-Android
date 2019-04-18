package com.example.imdb.data.database

import androidx.annotation.UiThread
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.MovieCategory
import com.example.imdb.auxiliary.EMPTY_MOVIE_DETAIL
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.data.entity.http.*
import com.example.imdb.data.entity.table.*
import kotlinx.coroutines.*

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
    private val language = ""
    fun getLanguage() = language

    init {
        setup()
    }

    fun setup() {
        coroutine {
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Latest.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.NowPlaying.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Popular.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.TopRated.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Upcoming.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Recommendation.ordinal, 1, 1))
        }
    }

    fun getMovieCredit(idMovie: Int, response: (MovieCredit) -> Unit)  {
        coroutine {
            val creditMovieDb = moviesDao().getMovieCreditCast(idMovie)
            val id = if(creditMovieDb.isEmpty()) 0 else idMovie
            val creditMovie = mutableListOf<Cast>()
            for (cast in creditMovieDb) {
                creditMovie.add(Cast(cast.idCast, cast.character, cast.gender, cast.id, cast.name, cast.order, cast.profilePath, error = false))
            }
            response(MovieCredit(creditMovie, id))
        }
    }

    fun getLatest(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Latest, response)

    fun getNowPlaying(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.NowPlaying, response)

    fun getPopular(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Popular, response)

    fun getTopRated(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.TopRated, response)

    fun getUpcoming(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Upcoming, response)

    fun getDetailMovie(idMovie: Int, response: (MovieDetail) -> Unit) {
        coroutine {
            val movieDetailDb = moviesDao().getMovieDetail(idMovie)
            if(movieDetailDb != null) {
                response(MovieDetail(
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
                ))
            }
            else { response(EMPTY_MOVIE_DETAIL) }
        }
    }

    fun getRecommendationLastMovie(idMovie: Int, response: (Recommendation) -> Unit) {
        coroutine {
            val moviesDb = moviesDao().getRecommendationMovie(idMovie)
            val movies = tableMoviesToMovies(moviesDb)
            if(movies.isEmpty())
                response(Recommendation(ZERO, MoviesList(ZERO, movies, ZERO)))
            else
                response(Recommendation(idMovie, MoviesList(ZERO, movies, ZERO)))
        }
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

            for (review in reviews) {
                val r = review.review
                listOfReviews.add(Review(r.author, r.content, r.idReview.toString(), r.url, false))
            }
            totalPages = reviewInformation.reviewInformation.totalPages
            totalResults = reviewInformation.reviewInformation.totalResults
            idMovie = reviewInformation.reviewInformation.idMovie_fk
        }

        return Reviews(id, page, listOfReviews.toList(), totalPages, totalResults, idMovie)
    }

    fun getLastTimeUpdateCategory(movieCategory: MovieCategory): Long {
        val last = moviesDao().getLastUpdateCategory(movieCategory.ordinal)
        return if(last != null) last.timeUpdate else System.currentTimeMillis()
    }

    fun getFavorites(): List<Movie> {
        val favoritesDb = moviesDao().getFavorite()
        return tableMoviesToMovies(favoritesDb)
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
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
            moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + movie.id, idMovie, movie.id))
        }
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id, movieDetail.overview, movieDetail.posterPath.toString(),
            movieDetail.releaseDate, movieDetail.runtime, movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
    }

    fun setLatest(movie: Movie) {
        if(movie.error)
            return

        moviesDao().deleteMovieCategory(MovieCategory.Latest.ordinal)
        moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
        moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Latest.ordinal))
    }

    fun setNowPlaying(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.NowPlaying.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.NowPlaying.ordinal))
        }
    }

    fun setPopular(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.Popular.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Popular.ordinal))
        }
    }

    fun setTopRated(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.TopRated.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.TopRated.ordinal))
        }
    }

    fun setUpcoming(movies: List<Movie>) {
        moviesDao().deleteMovieCategory(MovieCategory.Upcoming.ordinal)
        for (movie in movies) {
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult, false))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Upcoming.ordinal))
        }
    }

    fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        for (cast in credit.cast) {
            moviesDao().insertMovieCast(TableCast(cast.castId, idMovie, cast.character, cast.gender, cast.id, cast.name, cast.order, cast.profilePath))
        }
    }

    fun lastTimeUpdateCategory(movieCategory: MovieCategory, currentTime: Long) {
        moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(movieCategory.ordinal, currentTime))
    }

    fun favoriteMovie(movieId: Int, favorite: Boolean) { moviesDao().favoriteMovie(movieId, favorite) }

    private fun getMovies(movieCategory: MovieCategory, response: (MutableList<Movie>) -> Unit) : MutableList<Movie> {
        coroutine {
            val moviesDb = moviesDao().getMoviesListAndMovie(movieCategory.ordinal)
            response(tableMoviesToMovies(moviesDb))
        }
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for (movie in moviesDb) {
            movies.add(Movie(movie.idMovie, movie.originalTitle, movie.posterPath, movie.title, false, false, movie.adult, movie.favorite))
        }
        return movies
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) { block() }
    }

}