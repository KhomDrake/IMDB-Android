package com.example.imdb.data

import android.content.Context
import com.example.imdb.MovieCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.*
import com.example.imdb.network.WebController
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class DataController(private val webController: WebController, private val databaseMovies: DatabaseMovies) {

    private lateinit var language: String

    private val timeToBeDeprecated: Long = 5 * 60000

    fun setupDatabase(language: String) { this.language = language }

    fun getLanguage() = language

    fun getFavorites() = databaseMovies.getFavorites()

    fun favoriteMovie(movieId: Int, favorite: Boolean) = databaseMovies.favoriteMovie(movieId, favorite)

    fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
        val movieCredit = databaseMovies.getMovieCredit(id)

        if(id != movieCredit.id) {
            webController.loadMovieCredit(id) {
                databaseMovies.setCreditMovie(it, id)
                funResponse(it)
            }
        } else { funResponse(movieCredit) }
    }

    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
        val movieDetail = databaseMovies.getDetailMovie(id)

        if(movieDetail.id != id) {
            webController.loadMovieDetail(id) {
                if(!it.error) databaseMovies.setMovieDetail(it)
                funResponse(it)
            }
        } else { funResponse(movieDetail) }
    }

    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        val reviews = databaseMovies.getMovieReviews(id)
        if(reviews.idMovie != id) {
            webController.loadReviews(id) {
                it.idMovie = id
                if(it.results.isNotEmpty() && !it.results[0].error) databaseMovies.setReviews(it)
                funResponse(it)
            }
        } else { funResponse(reviews) }
    }

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        val recommendation = databaseMovies.getRecommendationLastMovie(id)
        if(recommendation.id != id) {
            webController.loadRecommendation(id) {
                if(it.results.isNotEmpty() && !it.results.first().error)
                    databaseMovies.setRecommendationLastMovie(Recommendation(id, it))
                funResponse(databaseMovies.getRecommendationLastMovie(id).moviesList.results)
            }
        } else { funResponse(recommendation.moviesList.results) }
    }

    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        val latest = databaseMovies.getLatest()
        if (latest.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Latest)) {
            webController.loadLatest {
                setListDatabaseMovies(listOf(it), MovieCategory.Latest)
                returnRightResponse(funResponse, listOf(it), MovieCategory.Latest)
            }
        } else { funResponse(latest) }
    }

    fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        val nowPlaying = databaseMovies.getNowPlaying()
        if (nowPlaying.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.NowPlaying)) {
            webController.loadNowPlaying {
                setListDatabaseMovies(it.results, MovieCategory.NowPlaying)
                returnRightResponse(funResponse, it.results, MovieCategory.NowPlaying)
            }
        } else { funResponse(nowPlaying) }
    }

    fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        val popular = databaseMovies.getPopular()
        if (popular.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Popular)) {
            webController.loadPopular {
                setListDatabaseMovies(it.results, MovieCategory.Popular)
                returnRightResponse(funResponse, it.results, MovieCategory.Popular)
            }
        } else { funResponse(popular) }
    }

    fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        val topRated = databaseMovies.getTopRated()
        if (topRated.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Popular)) {
            webController.loadTopRated {
                setListDatabaseMovies(it.results, MovieCategory.TopRated)
                returnRightResponse(funResponse, it.results, MovieCategory.TopRated)
            }
        } else { funResponse(topRated) }
    }

    fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        val upcoming = databaseMovies.getUpcoming()
        if (upcoming.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Upcoming)) {
            webController.loadUpcoming {
                setListDatabaseMovies(it.results, MovieCategory.Upcoming)
                returnRightResponse(funResponse, it.results, MovieCategory.Upcoming)
            }
        } else { funResponse(upcoming) }
    }

    private fun setLatest(movie: Movie) = databaseMovies.setLatest(movie)

    private fun setNowPlaying(movies: List<Movie>) = databaseMovies.setNowPlaying(movies)

    private fun setPopular(movies: List<Movie>) = databaseMovies.setPopular(movies)

    private fun setTopRated(movies: List<Movie>) = databaseMovies.setTopRated(movies)

    private fun setUpcoming(movies: List<Movie>) = databaseMovies.setUpcoming(movies)

    private fun setTime(movieCategory: MovieCategory) = databaseMovies.lastTimeUpdateCategory(movieCategory, getCurrentTime())

    private fun setListDatabaseMovies(movies: List<Movie>, movieCategory: MovieCategory) {
        val list: List<Movie> = if(movies.isEmpty() || movies[0].error) listOf() else movies

        if(list.isEmpty())
            return

        setTime(movieCategory)

        when(movieCategory) {
            MovieCategory.NowPlaying -> setNowPlaying(list)
            MovieCategory.Popular -> setPopular(list)
            MovieCategory.TopRated -> setTopRated(list)
            MovieCategory.Upcoming -> setUpcoming(list)
            MovieCategory.Latest -> setLatest(list[0])
            else -> Unit
        }
    }

    private fun returnRightResponse(funResponse: (movies: List<Movie>) -> Unit,
                                    movies: List<Movie>,
                                    movieCategory: MovieCategory) {
        when (movieCategory) {
            MovieCategory.Latest -> if (hasError(movies)) funResponse(databaseMovies.getLatest()) else funResponse(movies)
            MovieCategory.Upcoming -> if (hasError(movies)) funResponse(databaseMovies.getUpcoming()) else funResponse(movies)
            MovieCategory.TopRated -> if (hasError(movies)) funResponse(databaseMovies.getTopRated()) else funResponse(movies)
            MovieCategory.NowPlaying -> if (hasError(movies)) funResponse(databaseMovies.getNowPlaying()) else funResponse(movies)
            MovieCategory.Popular -> if (hasError(movies)) funResponse(databaseMovies.getNowPlaying()) else funResponse(movies)
            else -> Unit
        }
    }


    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun hasError(movies: List<Movie>) = movies.isNotEmpty() && !movies.first().error

    private fun dataIsDeprecated(movieCategory: MovieCategory) = deprecatedSinceLastUpdate(movieCategory)

    private fun deprecatedSinceLastUpdate(movieCategory: MovieCategory) = getCurrentTime()
        .minus(getTimeLastUpdate(movieCategory)) > timeToBeDeprecated

    private fun getTimeLastUpdate(movieCategory: MovieCategory) = databaseMovies.getLastTimeUpdateCategory(movieCategory)

    private fun getCurrentTime() = System.currentTimeMillis()

}
