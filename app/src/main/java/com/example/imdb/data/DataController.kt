package com.example.imdb.data

import android.content.Context
import android.content.SharedPreferences
import com.example.imdb.MovieCategory
import com.example.imdb.ctx
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.Movie
import com.example.imdb.data.entity.MovieDetail
import com.example.imdb.network.WebController

object DataController {

    private val sharedPreferences
        get() = ctx.getSharedPreferences("test", Context.MODE_PRIVATE)
    private lateinit var language: String
    private const val timeToBeDeprecated: Long = 5 * 60000

    fun setupDatabase(language: String) {
        this.language = language
    }

    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {

        val movieDetail = getDetailMovie()

        if(movieDetail.id != id) {
            WebController.loadMovieDetail(id) {
                DatabaseMovies.setMovieDetail(it)
                funResponse(it)
            }
        } else {
            funResponse(movieDetail)
        }
    }

    fun getLanguage() = language

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        val recommendation = DatabaseMovies.getRecommendationLastMovie()
        if(recommendation.id != id) {

        } else {
            funResponse(recommendation.moviesList.results)
        }
    }

    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        val latest = getLatest()
        if (latest.isEmptyOrInLoading() || dataIsDeprecated("latest")) {
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
        if (nowPlaying.isEmptyOrInLoading() || dataIsDeprecated("nowplaying")) {
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
        if (popular.isEmptyOrInLoading() || dataIsDeprecated("popular")) {
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
        if (topRated.isEmptyOrInLoading() || dataIsDeprecated("toprated")) {
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
        if (upcoming.isEmptyOrInLoading() || dataIsDeprecated("upcoming")) {
            WebController.loadUpcoming {
                setListDatabaseMovies(it.results, MovieCategory.Upcoming)
                funResponse(it.results)
            }
        } else {
            funResponse(upcoming)
        }
    }

    private fun getDetailMovie() = DatabaseMovies.getDetailMovie()

    private fun getLatest() = DatabaseMovies.getLatest()

    private fun getNowPlaying() = DatabaseMovies.getNowPlaying()

    private fun getPopular() = DatabaseMovies.getPopular()

    private fun getTopRated() = DatabaseMovies.getTopRated()

    private fun getUpcoming() = DatabaseMovies.getUpcoming()

    private fun setLatest(movie: Movie) = DatabaseMovies.setLatest(movie)

    private fun setNowPlaying(movies: List<Movie>) = DatabaseMovies.setNowPlaying(movies)

    private fun setPopular(movies: List<Movie>) = DatabaseMovies.setPopular(movies)

    private fun setTopRated(movies: List<Movie>) = DatabaseMovies.setTopRated(movies)

    private fun setUpcoming(movies: List<Movie>) = DatabaseMovies.setUpcoming(movies)

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

    private fun dataIsDeprecated(type: String) : Boolean {
        if(!existLastUpdate(type))
            setTime(type)

        return deprecatedSinceLastUpdate(type)
    }

    private fun deprecatedSinceLastUpdate(type: String) = getCurrentTime()
        .minus(getTimeLastUpdate(type)) > timeToBeDeprecated

    private fun getTimeLastUpdate(type: String) = sharedPreferences.all["lastUpdateTable$type"].toString().toLong()

    private fun existLastUpdate(type: String) = sharedPreferences.all["lastUpdateTable$type"] != null

    private fun getCurrentTime() = System.currentTimeMillis()

    private fun setTime(type: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong("lastUpdateTable$type", System.currentTimeMillis())
        editor.apply()
    }

    private fun setListDatabaseMovies(movies: List<Movie>, movieCategory: MovieCategory) {
        val list: List<Movie> = if(movies.isEmpty() || movies[0].error)
            listOf()
        else
            movies

        when(movieCategory) {
            MovieCategory.NowPlaying -> {
                if(list.count() != 0)
                    setTime("nowplaying")
                setNowPlaying(list)
            }

            MovieCategory.Popular -> {
                if(list.count() != 0)
                    setTime("popular")
                setPopular(list)
            }

            MovieCategory.TopRated -> {
                if(list.count() != 0)
                    setTime("toprated")
                setTopRated(list)
            }

            MovieCategory.Upcoming -> {
                if(list.count() != 0)
                    setTime("upcoming")
                setUpcoming(list)
            }
        }
    }

    private fun setListDatabaseMovie(movie: Movie) {
        if(!movie.error) {
            setTime("latest")
            setLatest(movie)
        }
        else
            setLatest(movie = Movie( 0,"", "", "", loading = false, error = true))
    }
}
