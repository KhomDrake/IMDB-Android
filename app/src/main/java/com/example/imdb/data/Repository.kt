package com.example.imdb.data

import android.util.Log
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(private val webController: API, private val databaseMovies: DatabaseMovies) {

    private lateinit var language: String

    private val timeToBeDeprecated: Long = 5 * 60000

    fun setupDatabase(language: String) { this.language = language }

    fun getFavorites(response: (List<Movie>) -> Unit) {
        coroutine {
            response(databaseMovies.getFavorites())
        }
    }

    fun favoriteMovie(movieId: Int, toFavorite: Boolean) = coroutine {
        databaseMovies.favoriteMovie(movieId, toFavorite)
    }

    fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
        coroutine {
            val movieCredit = databaseMovies.getMovieCredit(id)
            if(id != movieCredit.id) {
                val movieCreditAPI = webController.loadMovieCredit(id)
                databaseMovies.setCreditMovie(movieCreditAPI, id)
                funResponse(movieCreditAPI)
            } else { funResponse(movieCredit) }
        }
    }

    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
        coroutine {
            val movieDetail = databaseMovies.getDetailMovie(id)
            if(movieDetail.id != id) {
                val movieDetailAPI = webController.loadMovieDetail(id)
                databaseMovies.setMovieDetail(movieDetailAPI)
                funResponse(movieDetailAPI)
            } else { funResponse(movieDetail) }
        }
    }

    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        coroutine {
            val movieReviews = databaseMovies.getMovieReviews(id)
            if(movieReviews.idMovie != id) {
                val movieReviewsAPI = webController.loadReviews(id)
                Log.i(TAG_VINI, movieReviewsAPI.toString())
                databaseMovies.setReviews(movieReviewsAPI)
                funResponse(movieReviewsAPI)
            }  else { funResponse(movieReviews) }
        }
    }

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        coroutine {
            val recommendation = databaseMovies.getRecommendationLastMovie(id)
            val favorites = databaseMovies.getFavorites()
            if(recommendation.id != id) {
                val recommendationAPI = webController.loadRecommendation(id)
                databaseMovies.setRecommendationMovie(Recommendation(id, recommendationAPI))
                funResponse(moviesAreFavorites(recommendationAPI.results, favorites))
            } else { funResponse(moviesAreFavorites(recommendation.moviesList.results, favorites)) }
        }
    }

    fun loadMovieCategory(movieDbCategory: MovieDbCategory, funResponse: (movies: List<Movie>) -> Unit) {
        coroutine {
            val movies = databaseMovies.getCategory(movieDbCategory)
            val favorites = databaseMovies.getFavorites()
            val isToMake = isToMakeAPIRequest(movies, movieDbCategory)
            if(isToMake) {
                val listMovieAPI = webController.loadCategory(movieDbCategory)
                databaseMovies.setMovie(listMovieAPI.results, movieDbCategory)
                if(listMovieAPI.results.isNotEmpty() && listMovieAPI.results.first().error) {
                    funResponse(databaseMovies.getCategory(movieDbCategory))
                } else {
                    setLastTimeUpdateCategory(movieDbCategory)
                    funResponse(listMovieAPI.results)
                }
            } else { funResponse(moviesAreFavorites(movies, favorites)) }
        }
    }

    private fun setLastTimeUpdateCategory(movieDbCategory: MovieDbCategory) = databaseMovies.lastTimeUpdateCategory(movieDbCategory, getCurrentTime())

    private fun List<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun isToMakeAPIRequest(movieList: List<Movie>, movieDbCategory: MovieDbCategory) : Boolean {
        val lastTime = databaseMovies.getLastTimeUpdateCategory(movieDbCategory)
        val deprecated = getCurrentTime().minus(lastTime) > timeToBeDeprecated
        return movieList.isEmptyOrInLoading() || deprecated
    }

    private fun getCurrentTime() = System.currentTimeMillis()

    private fun moviesAreFavorites(movies: List<Movie>, favorites: List<Movie>) : List<Movie> {
        for(movie in movies) {
            if(favorites.contains(movie)) {
                movie.favorite = true
            }
        }
        return movies
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            block()
        }
    }
}
