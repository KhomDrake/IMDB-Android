package com.example.imdb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.MovieDbCategory
import com.example.imdb.auxiliary.EMPTY_MOVIE_DETAIL
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.data.entity.application.RightResponseMovieCategory
import com.example.imdb.data.entity.http.Review
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.data.entity.table.*
import kotlinx.coroutines.*

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

    fun getMovieCredit(idMovie: Int, response: (MovieCredit) -> Unit)  {
        coroutine {
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

            response(MovieCredit(creditMovie, id))
        }
    }

    fun getLatest(response: (MutableList<Movie>) -> Unit) = getMovies(MovieDbCategory.MovieLatest, response)

    fun getNowPlaying(response: (MutableList<Movie>) -> Unit) = getMovies(MovieDbCategory.MovieNowPlaying, response)

    fun getPopular(response: (MutableList<Movie>) -> Unit) = getMovies(MovieDbCategory.MoviePopular, response)

    fun getTopRated(response: (MutableList<Movie>) -> Unit) = getMovies(MovieDbCategory.MovieTopRated, response)

    fun getUpcoming(response: (MutableList<Movie>) -> Unit) = getMovies(MovieDbCategory.MovieUpcoming, response)

    fun getDetailMovie(idMovie: Int, response: (MovieDetail) -> Unit) {
        coroutine {
            val movieDetailDb = moviesDao().getMovieDetail(idMovie)
            if(movieDetailDb != null) {
                response(
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
                )
            }
            else { response(EMPTY_MOVIE_DETAIL) }
        }
    }

    fun getRecommendationLastMovie(idMovie: Int, response: (Recommendation) -> Unit) {
        coroutine {
            val moviesDb = moviesDao().getRecommendationMovie(idMovie)
            val movies = tableMoviesToMovies(moviesDb)
            var recommendation = Recommendation(
                ZERO,
                MoviesList(ZERO, movies, ZERO)
            )
            if(movies.isNotEmpty())
                recommendation = Recommendation(
                    idMovie,
                    MoviesList(ZERO, movies, ZERO)
                )

            response(recommendation)
        }
    }

    fun getMovieReviews(_idMovie: Int, response: (Reviews) -> Unit) {
        coroutine {
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
                    listOfReviews.add(
                        Review(
                            r.author,
                            r.content,
                            r.idReview.toString(),
                            r.url,
                            false
                        )
                    )
                }
                totalPages = reviewInformation.reviewInformation.totalPages
                totalResults = reviewInformation.reviewInformation.totalResults
                idMovie = reviewInformation.reviewInformation.idMovie_fk
            }

            response(
                Reviews(
                    id,
                    page,
                    listOfReviews.toList(),
                    totalPages,
                    totalResults,
                    idMovie
                )
            )
        }
    }

    fun getLastTimeUpdateCategory(movieDbCategory: MovieDbCategory, response: (Long) -> Unit) {
        coroutine {
            val last = moviesDao().getLastUpdateCategory(movieDbCategory.ordinal)
            response(if(last != null) last.timeUpdate else System.currentTimeMillis())
        }
    }

    fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        coroutine {
            val favoritesDb = moviesDao().getFavorite()
            val favorites = tableMoviesToMovies(favoritesDb)
            response(favorites)
        }
    }

    fun setReviews(reviews: Reviews) {
        coroutine {
            moviesDao().insertReviewInformation(TableReviewInformation(reviews.id, reviews.idMovie, 1, 1, 1))
            for(review in reviews.results) {
                moviesDao().insertReview(TableReview(0, review.author, review.content, review.url, reviews.id))
            }
        }
    }

    fun setRecommendationLastMovie(recommendation: Recommendation) {
        val idMovie = recommendation.id
        coroutine {
            for (movie in recommendation.moviesList.results) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + movie.id, idMovie, movie.id))
            }
        }
    }

    fun setMovieDetail(movieDetail: MovieDetail) {
        coroutine {
            moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id,
                movieDetail.overview, movieDetail.posterPath.toString(), movieDetail.releaseDate, movieDetail.runtime,
                movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
        }
    }

    fun setLatest(movie: Movie, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                  rightResponseMovieCategory: RightResponseMovieCategory) {
        if(movie.error)
            return

        coroutine {
            moviesDao().deleteMovieCategory(MovieDbCategory.MovieLatest.ordinal)
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieDbCategory.MovieLatest.ordinal))
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    fun setNowPlaying(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                      rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieDbCategory.MovieNowPlaying.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieDbCategory.MovieNowPlaying.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    fun setPopular(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                   rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieDbCategory.MoviePopular.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieDbCategory.MoviePopular.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    fun setTopRated(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                    rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieDbCategory.MovieTopRated.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieDbCategory.MovieTopRated.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    fun setUpcoming(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                    rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieDbCategory.MovieUpcoming.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieDbCategory.MovieUpcoming.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        coroutine {
            for (cast in credit.castMovie) {
                moviesDao().insertMovieCast(TableCast(cast.castId, idMovie, cast.character, cast.gender,
                    cast.id, cast.name, cast.order, cast.profilePath))
            }
        }
    }

    fun lastTimeUpdateCategory(movieDbCategory: MovieDbCategory, currentTime: Long) {
        coroutine {
            moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(movieDbCategory.ordinal, currentTime))
        }
    }

    fun favoriteMovie(movieId: Int, toFavorite: Boolean) {
        coroutine {
            if(toFavorite)
                moviesDao().insertFavorite(TableFavorite(movieId))
            else
                moviesDao().deleteFavorite(movieId)
        }
    }

    private fun setup() {
        coroutine {
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MovieLatest.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MovieNowPlaying.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MoviePopular.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MovieTopRated.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MovieUpcoming.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieDbCategory.MovieRecommendation.ordinal, 1, 1))
        }
    }

    private fun getMovies(movieDbCategory: MovieDbCategory, response: (MutableList<Movie>) -> Unit) {
        coroutine {
            val moviesDb = moviesDao().getMoviesListAndMovie(movieDbCategory.ordinal)
            response(tableMoviesToMovies(moviesDb))
        }
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : MutableList<Movie> {
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
        return movies
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            block()
        }
    }
}