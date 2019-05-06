package com.example.imdb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imdb.MovieCategory
import com.example.imdb.auxiliary.EMPTY_MOVIE_DETAIL
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.data.entity.application.RightResponseMovieCategory
import com.example.imdb.data.entity.http.*
import com.example.imdb.data.entity.table.*
import kotlinx.coroutines.*

@Database(entities = arrayOf(TableMovie::class,
                            TableMovieCategory::class,
                            TableMovieDetail::class,
                            TableMoviesList::class,
                            TableReview::class,
                            TableFavorite::class,
                            TableLastUpdateCategory::class,
                            TableCast::class,
                            TableMovieRecommendation::class,
                            TableReviewInformation::class), version = 1)
abstract class DatabaseMovies : RoomDatabase(), IDatabaseMovies {

    abstract fun moviesDao(): DaoMovies
    private val language = ""

    init {
        setup()
    }

    private fun setup() {
        coroutine {
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Latest.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.NowPlaying.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Popular.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.TopRated.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Upcoming.ordinal, 1, 1))
            moviesDao().insertMovieList(TableMoviesList(MovieCategory.Recommendation.ordinal, 1, 1))
        }
    }

    override fun getLanguage() = language

    override fun getMovieCredit(idMovie: Int, response: (MovieCredit) -> Unit)  {
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

    override fun getLatest(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Latest, response)

    override fun getNowPlaying(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.NowPlaying, response)

    override fun getPopular(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Popular, response)

    override fun getTopRated(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.TopRated, response)

    override fun getUpcoming(response: (MutableList<Movie>) -> Unit) = getMovies(MovieCategory.Upcoming, response)

    override fun getDetailMovie(idMovie: Int, response: (MovieDetail) -> Unit) {
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

    override fun getRecommendationLastMovie(idMovie: Int, response: (Recommendation) -> Unit) {
        coroutine {
            val moviesDb = moviesDao().getRecommendationMovie(idMovie)
            val movies = tableMoviesToMovies(moviesDb)
            var recommendation = Recommendation(ZERO, MoviesList(ZERO, movies, ZERO))
            if(movies.isNotEmpty())
                recommendation = Recommendation(idMovie, MoviesList(ZERO, movies, ZERO))

            response(recommendation)
        }
    }

    override fun getMovieReviews(_idMovie: Int, response: (Reviews) -> Unit) {
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
                    listOfReviews.add(Review(r.author, r.content, r.idReview.toString(), r.url, false))
                }
                totalPages = reviewInformation.reviewInformation.totalPages
                totalResults = reviewInformation.reviewInformation.totalResults
                idMovie = reviewInformation.reviewInformation.idMovie_fk
            }

            response(Reviews(id, page, listOfReviews.toList(), totalPages, totalResults, idMovie))
        }
    }

    override fun getLastTimeUpdateCategory(movieCategory: MovieCategory, response: (Long) -> Unit) {
        coroutine {
            val last = moviesDao().getLastUpdateCategory(movieCategory.ordinal)
            response(if(last != null) last.timeUpdate else System.currentTimeMillis())
        }
    }

    override fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        coroutine {
            val favoritesDb = moviesDao().getFavorite()
            val favorites = tableMoviesToMovies(favoritesDb)
            response(favorites)
        }
    }

    override fun setReviews(reviews: Reviews) {
        coroutine {
            moviesDao().insertReviewInformation(TableReviewInformation(reviews.id, reviews.idMovie, 1, 1, 1))
            for(review in reviews.results) {
                moviesDao().insertReview(TableReview(0, review.author, review.content, review.url, reviews.id))
            }
        }
    }

    override fun setRecommendationLastMovie(recommendation: Recommendation) {
        val idMovie = recommendation.id
        coroutine {
            for (movie in recommendation.moviesList.results) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieRecommendation(TableMovieRecommendation(idMovie + movie.id, idMovie, movie.id))
            }
        }
    }

    override fun setMovieDetail(movieDetail: MovieDetail) {
        coroutine {
            moviesDao().insertMovieDetail(TableMovieDetail(movieDetail.id, movieDetail.adult, movieDetail.id,
                movieDetail.overview, movieDetail.posterPath.toString(), movieDetail.releaseDate, movieDetail.runtime,
                movieDetail.title, movieDetail.voteAverage, movieDetail.voteCount))
        }
    }

    override fun setLatest(movie: Movie, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                           rightResponseMovieCategory: RightResponseMovieCategory) {
        if(movie.error)
            return

        coroutine {
            moviesDao().deleteMovieCategory(MovieCategory.Latest.ordinal)
            moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
            moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Latest.ordinal))
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    override fun setNowPlaying(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                               rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieCategory.NowPlaying.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.NowPlaying.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    override fun setPopular(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                            rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieCategory.Popular.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Popular.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    override fun setTopRated(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                             rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieCategory.TopRated.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.TopRated.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    override fun setUpcoming(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit,
                    rightResponseMovieCategory: RightResponseMovieCategory) {
        coroutine {
            moviesDao().deleteMovieCategory(MovieCategory.Upcoming.ordinal)
            for (movie in movies) {
                moviesDao().insertMovie(TableMovie(movie.id, movie.originalTitle, movie.posterPath, movie.title, movie.adult))
                moviesDao().insertMovieCategory(TableMovieCategory(ZERO, movie.id, MovieCategory.Upcoming.ordinal))
            }
            returnRightResponse(rightResponseMovieCategory)
        }
    }

    override fun setCreditMovie(credit: MovieCredit, idMovie: Int) {
        coroutine {
            for (cast in credit.cast) {
                moviesDao().insertMovieCast(TableCast(cast.castId, idMovie, cast.character, cast.gender,
                    cast.id, cast.name, cast.order, cast.profilePath))
            }
        }
    }

    override fun lastTimeUpdateCategory(movieCategory: MovieCategory, currentTime: Long) {
        coroutine {
            moviesDao().insertLastUpdateCategory(TableLastUpdateCategory(movieCategory.ordinal, currentTime))
        }
    }

    override fun favoriteMovie(movieId: Int, toFavorite: Boolean) {
        coroutine {
            if(toFavorite)
                moviesDao().insertFavorite(TableFavorite(movieId))
            else
                moviesDao().deleteFavorite(movieId)
        }
    }

    private fun getMovies(movieCategory: MovieCategory, response: (MutableList<Movie>) -> Unit) {
        coroutine {
            val moviesDb = moviesDao().getMoviesListAndMovie(movieCategory.ordinal)
            response(tableMoviesToMovies(moviesDb))
        }
    }

    private fun tableMoviesToMovies(moviesDb: List<TableMovie>) : MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for (movie in moviesDb) {
            movies.add(Movie(movie.idMovie, movie.originalTitle, movie.posterPath, movie.title, false, false, movie.adult, false))
        }
        return movies
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            block()
        }
    }
}