package com.example.imdb.data

import android.util.Log
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.TheMovieDbCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.LoginBody
import com.example.imdb.data.entity.http.Rate
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(private val api: API, private val databaseMovies: DatabaseMovies) {

    private lateinit var language: String

    private val timeToBeDeprecated: Long = 5 * 60000

    init {
        databaseMovies.setup()
    }

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
                val movieCreditAPI = api.loadMovieCredit(id)
                databaseMovies.setCreditMovie(movieCreditAPI, id)
                funResponse(movieCreditAPI)
            } else { funResponse(movieCredit) }
        }
    }

    fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
        coroutine {
            val movieDetail = databaseMovies.getDetailMovie(id)
            if(movieDetail.id != id) {
                val movieDetailAPI = api.loadMovieDetail(id)
                databaseMovies.setMovieDetail(movieDetailAPI)
                funResponse(movieDetailAPI)
            } else { funResponse(movieDetail) }
        }
    }

    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        coroutine {
            val movieReviews = databaseMovies.getMovieReviews(id)
            if(movieReviews.idReviewed != id) {
                val movieReviewsAPI = api.loadReviews(id)
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
                val recommendationAPI = api.loadRecommendation(id)
                databaseMovies.setRecommendationMovie(Recommendation(id, recommendationAPI))
                funResponse(moviesAreFavorites(recommendationAPI.results, favorites))
            } else { funResponse(moviesAreFavorites(recommendation.moviesList.results, favorites)) }
        }
    }

    fun loadMovieCategory(theMovieDbCategory: TheMovieDbCategory, funResponse: (movies: List<Movie>) -> Unit, pag: Int = 1) {
        coroutine {
            if(pag != 1) {
                val listMovieAPI = api.loadCategory(theMovieDbCategory, pag)
                val favorites = databaseMovies.getFavorites()
                databaseMovies.setMovie(listMovieAPI.results, theMovieDbCategory)
                funResponse(moviesAreFavorites(listMovieAPI.results, favorites))
                return@coroutine
            }
            val movies = databaseMovies.getCategory(theMovieDbCategory)
            val favorites = databaseMovies.getFavorites()
            val isToMake = isToMakeAPIRequest(movies, theMovieDbCategory)
            if(isToMake) {
                val listMovieAPI = api.loadCategory(theMovieDbCategory, pag)
                if(listMovieAPI.results.isEmpty() && listMovieAPI.results.first().error) {
                    funResponse(databaseMovies.getCategory(theMovieDbCategory))
                } else {
                    databaseMovies.setMovie(listMovieAPI.results, theMovieDbCategory)
                    setLastTimeUpdateCategory(theMovieDbCategory)
                    funResponse(moviesAreFavorites(listMovieAPI.results, favorites))
                }
            } else { funResponse(moviesAreFavorites(movies, favorites)) }
        }
    }

    private fun setLastTimeUpdateCategory(theMovieDbCategory: TheMovieDbCategory) = databaseMovies.lastTimeUpdateCategory(theMovieDbCategory, getCurrentTime())

    private fun List<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun isToMakeAPIRequest(movieList: List<Movie>, theMovieDbCategory: TheMovieDbCategory) : Boolean {
        val lastTime = databaseMovies.getLastTimeUpdateCategory(theMovieDbCategory)
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

    fun getSessionId(response: (String) -> Unit) {
        coroutine {
            val session = api.getSessionId()
            response(session.guestSessionId)
        }
    }

    fun rateMovie(idMovie: Int, body: Rate, response: (String) -> Unit) {
        coroutine {
            val resultado = api.rateMovie(idMovie, body, Session.getSessionId(), Session.getIsLoginAsGuest())
            val mensagem = if(resultado == "Success.") {
                "Avaliado com sucesso"
            } else {
                "Avaliação não foi concluída"
            }

            response(mensagem)
        }
    }

    fun validLogin(body: LoginBody, response: (Boolean, String) -> Unit) {
        coroutine {
            try {
                val requestToken = api.createRequestToken()
                body.requestToken = requestToken.requestToken
                val loginValid = api.createSessionWithLogin(body)
                if(!loginValid.success) {
                    response(false, "Dados incorretos")
                }
                val sessionId = api.createSession(requestToken.requestToken)
                Session.setSessionId(sessionId.sessionId)
                Session.loginAsUser()
            } catch (e: Exception) {
                Log.i(TAG_VINI, "erro: ${e.message}")
                response(false, "Falha ao tentar Logar")
            }
        }
    }
}