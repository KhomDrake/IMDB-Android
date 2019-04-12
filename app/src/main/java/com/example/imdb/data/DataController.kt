package com.example.imdb.data

import android.content.Context
import android.util.Log
import com.example.imdb.MovieCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.*
import com.example.imdb.network.WebController

object DataController {

    private lateinit var databaseMovies: DatabaseMovies

    private lateinit var language: String

    private const val timeToBeDeprecated: Long = 5 * 60000

    fun createDatabase(ctx: Context) {
        DatabaseMovies.instance(ctx)
        databaseMovies = DatabaseMovies.Instance!!
        databaseMovies.setup()
    }

    fun setupDatabase(language: String) {
        this.language = language
    }

    fun getLanguage() = language

    fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
        val movieCredit = databaseMovies.getMovieCredit(id)
        if(id != movieCredit.id) {
            WebController.loadMovieCredit(id) {
                databaseMovies.setCreditMovie(it, id)
                funResponse(it)
            }
        } else {
            funResponse(movieCredit)
        }
    }

    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {

        val movieDetail = getDetailMovie(id)

        if(movieDetail.id != id) {
            WebController.loadMovieDetail(id) {
                if(!it.error)
                    databaseMovies.setMovieDetail(it)
                funResponse(it)
            }
        } else {
            funResponse(movieDetail)
        }
    }

    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        val reviews = databaseMovies.getMovieReviews(id)
        if(reviews.idMovie != id) {
            WebController.loadReviews(id) {
                it.idMovie = id
                databaseMovies.setReviews(it)
                funResponse(it)
            }
        } else {
            funResponse(reviews)
        }
    }

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        val recommendation = databaseMovies.getRecommendationLastMovie(id)

        if(recommendation.id != id) {
            WebController.loadRecommendation(id) {
                Log.i("vini", "alksjdlkasjd")
                databaseMovies.setRecommendationLastMovie(Recommendation(id, it))
                funResponse(it.results)
            }
        } else {
            funResponse(recommendation.moviesList.results)
        }
    }

    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        val latest = getLatest()
        if (latest.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Latest)) {
            WebController.loadLatest {
                setListDatabaseMovie(it)
                funResponse(listOf(it))
            }
        } else {
            funResponse(latest)
        }
    }

    fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        val nowPlaying = getNowPlaying()
        if (nowPlaying.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.NowPlaying)) {
            WebController.loadNowPlaying {
                setListDatabaseMovies(it.results, MovieCategory.NowPlaying)
                funResponse(it.results)
            }
        } else {
            funResponse(nowPlaying)
        }
    }

    fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        val popular = getPopular()
        if (popular.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Popular)) {
            WebController.loadPopular {
                setListDatabaseMovies(it.results, MovieCategory.Popular)
                funResponse(it.results)
            }
        } else {
            funResponse(popular)
        }
    }

    fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        val topRated = getTopRated()
        if (topRated.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Popular)) {
            WebController.loadTopRated {
                setListDatabaseMovies(it.results, MovieCategory.TopRated)
                funResponse(it.results)
            }
        } else {
            funResponse(topRated)
        }
    }

    fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        val upcoming = getUpcoming()
        if (upcoming.isEmptyOrInLoading() || dataIsDeprecated(MovieCategory.Upcoming)) {
            WebController.loadUpcoming {
                setListDatabaseMovies(it.results, MovieCategory.Upcoming)
                funResponse(it.results)
            }
        } else {
            funResponse(upcoming)
        }
    }

    private fun setLatest(movie: Movie) = databaseMovies.setLatest(movie)

    private fun setNowPlaying(movies: List<Movie>) = databaseMovies.setNowPlaying(movies)

    private fun setPopular(movies: List<Movie>) = databaseMovies.setPopular(movies)

    private fun setTopRated(movies: List<Movie>) = databaseMovies.setTopRated(movies)

    private fun setUpcoming(movies: List<Movie>) = databaseMovies.setUpcoming(movies)

    private fun setTime(movieCategory: MovieCategory) {
        databaseMovies.lastTimeUpdateCategory(movieCategory, getCurrentTime())
    }

    private fun setListDatabaseMovies(movies: List<Movie>, movieCategory: MovieCategory) {
        val list: List<Movie> = if(movies.isEmpty() || movies[0].error)
            listOf()
        else
            movies

        when(movieCategory) {
            MovieCategory.NowPlaying -> {
                if(list.count() != 0)
                    setTime(MovieCategory.NowPlaying)
                setNowPlaying(list)
            }

            MovieCategory.Popular -> {
                if(list.count() != 0)
                    setTime(MovieCategory.Popular)
                setPopular(list)
            }

            MovieCategory.TopRated -> {
                if(list.count() != 0)
                    setTime(MovieCategory.TopRated)
                setTopRated(list)
            }

            MovieCategory.Upcoming -> {
                if(list.count() != 0)
                    setTime(MovieCategory.Upcoming)
                setUpcoming(list)
            }
        }
    }

    private fun setListDatabaseMovie(movie: Movie) {
        if(!movie.error) {
            setTime(MovieCategory.Latest)
            setLatest(movie)
        }
        else
            setLatest(movie = Movie(0, "", "", "", loading = false, error = true, adult = false))
    }

    private fun getDetailMovie(idMovie: Int) = databaseMovies.getDetailMovie(idMovie)

    private fun getLatest() = databaseMovies.getLatest()

    private fun getNowPlaying() = databaseMovies.getNowPlaying()

    private fun getPopular() = databaseMovies.getPopular()

    private fun getTopRated() = databaseMovies.getTopRated()

    private fun getUpcoming() = databaseMovies.getUpcoming()

    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) {
            if (movie.loading) {
                return true
            }
        }

        if (this.count() == 0)
            return true

        return false
    }

    private fun dataIsDeprecated(movieCategory: MovieCategory) : Boolean {

        return deprecatedSinceLastUpdate(movieCategory)
    }

    private fun deprecatedSinceLastUpdate(movieCategory: MovieCategory) = getCurrentTime()
        .minus(getTimeLastUpdate(movieCategory)) > timeToBeDeprecated

    private fun getTimeLastUpdate(movieCategory: MovieCategory) = databaseMovies.getLastTimeUpdateCategory(movieCategory)

    private fun getCurrentTime() = System.currentTimeMillis()
}
