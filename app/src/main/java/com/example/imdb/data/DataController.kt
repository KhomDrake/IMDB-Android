package com.example.imdb.data

import android.content.Context
import android.content.SharedPreferences
import com.example.imdb.ctx
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.Movie
import com.example.imdb.network.WebController

object DataController {

    private val sharedPreferences
        get() = ctx.getSharedPreferences("test", Context.MODE_PRIVATE)
    private lateinit var language: String
    private const val timeToBeDeprecated: Long = 700000

    fun setupDatabase(language: String) {
        this.language = language
    }

    fun getLanguage() = language

    fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        val latest = getLatest()
        funResponse(latest)
        if (latest.isEmptyOrInLoading() || dataIsDeprecated("latest")) {
            WebController.loadLatest {
                setLatest(it)
                funResponse(listOf(it))
            }
        }
    }

    fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        val nowPlaying = getNowPlaying()
        funResponse(nowPlaying)
        if (nowPlaying.isEmptyOrInLoading() || dataIsDeprecated("nowplaying")) {
            WebController.loadNowPlaying {
                setNowPlaying(it.results)
                funResponse(it.results)
            }
        }

    }

    fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        val popular = getPopular()
        funResponse(popular)
        if (popular.isEmptyOrInLoading() || dataIsDeprecated("popular")) {
            WebController.loadPopular {
                setPopular(it.results)
                funResponse(it.results)
            }
        }
    }

    fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        val topRated = getTopRated()
        funResponse(topRated)
        if (topRated.isEmptyOrInLoading() || dataIsDeprecated("toprated")) {
            WebController.loadTopRated {
                setTopRated(it.results)
                funResponse(it.results)
            }
        }
    }

    fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        val upcoming = getUpcoming()
        funResponse(upcoming)
        if (upcoming.isEmptyOrInLoading() || dataIsDeprecated("upcoming")) {
            WebController.loadUpcoming {
                setUpcoming(it.results)
                funResponse(it.results)
            }
        }
    }

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

        val result = deprecatedSinceLastUpdate(type)

        if(result) {
            setTime(type)
        }
        return result
    }

    private fun deprecatedSinceLastUpdate(type: String) = getCurrentTime()
        .minus(getTimeLastUpdate(type)) > timeToBeDeprecated

    private fun getTimeLastUpdate(type: String) = if(existLastUpdate(type)) 60 * 60000
        else sharedPreferences.all["lastUpdateTable$type"].toString().toLong()

    private fun existLastUpdate(type: String) = sharedPreferences.all["lastUpdateTable$type"] != null

    private fun getCurrentTime() = System.currentTimeMillis()

    private fun setTime(type: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong("lastUpdateTable$type", System.currentTimeMillis())
        editor.apply()
    }
}
