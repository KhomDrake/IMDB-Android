package com.example.imdb.data

import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.application.RightResponseMovieCategory
import com.example.imdb.data.entity.http.Reviews
import com.example.imdb.data.entity.http.movie.*
import com.example.imdb.network.IWebController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DataController(private val webController: IWebController, private val databaseMovies: DatabaseMovies) : IDataController {

    private lateinit var language: String

    private val timeToBeDeprecated: Long = 5 * 60000

    override fun setupDatabase(language: String) { this.language = language }

    override fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        databaseMovies.getFavorites(response)
    }

    override fun favoriteMovie(movieId: Int, toFavorite: Boolean) = databaseMovies.favoriteMovie(movieId, toFavorite)

    override fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
        coroutine {
            val movieCredit = databaseMovies.getMovieCredit(id)
            if(id != movieCredit.id) {
                webController.loadMovieCredit(id)
                    webController.loadMovieCredit(id) {
                        databaseMovies.setCreditMovie(it, id)
                        funResponse(it)
                    }
                } else { funResponse(movieCredit) }
            }
        }
    }

    override fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
        coroutine {
            databaseMovies.getDetailMovie(id) {
                val movieDetail = it
                if(movieDetail.id != id) {
                    webController.loadMovieDetail(id) {
                        if(!it.error) databaseMovies.setMovieDetail(it)
                        funResponse(it)
                    }
                } else { funResponse(movieDetail) }
            }
        }
    }

    override fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        coroutine {
            databaseMovies.getMovieReviews(id) {
                val reviews = it
                if(reviews.idMovie != id) {
                    webController.loadReviews(id) {
                        it.idMovie = id
                        if(it.results.isNotEmpty() && !it.results[0].error) databaseMovies.setReviews(it)
                        funResponse(it)
                    }
                } else { funResponse(reviews) }
            }
        }
    }

    override fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        coroutine {
            databaseMovies.getRecommendationLastMovie(id) {
                val recommendation = it
                databaseMovies.getFavorites {
                    val listFavorites = it
                    if(recommendation.id != id) {
                        webController.loadRecommendation(id) {
                            val moviesList = it
                            if(moviesList.results.isNotEmpty() && !moviesList.results.first().error) {
                                databaseMovies.setRecommendationLastMovie(
                                    Recommendation(
                                        id,
                                        moviesList
                                    )
                                )
                            }
                            funResponse(moviesAreFavorites(moviesList.results, listFavorites))
                        }
                    } else { funResponse(moviesAreFavorites(recommendation.moviesList.results, listFavorites)) }
                }
            }
        }
    }

    override fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getLatest {
            val latest = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(latest, MovieDbCategory.MovieLatest) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieDbCategory.MovieLatest) }
                    else { funResponse(moviesAreFavorites(latest, listFavorites)) }
                }
            }
        }
    }

    override fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getNowPlaying {
            val nowPlaying = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(nowPlaying, MovieDbCategory.MovieNowPlaying) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieDbCategory.MovieNowPlaying) }
                    else { funResponse(moviesAreFavorites(nowPlaying, listFavorites)) }
                }
            }
        }
    }

    override fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getPopular {
            val popular = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(popular, MovieDbCategory.MoviePopular) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieDbCategory.MoviePopular) }
                    else { funResponse(moviesAreFavorites(popular, listFavorites)) }
                }
            }
        }
    }

    override fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getTopRated {
            val topRated = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(topRated, MovieDbCategory.MovieTopRated) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieDbCategory.MovieTopRated) }
                    else { funResponse(moviesAreFavorites(topRated, listFavorites)) }
                }
            }
        }
    }

    override fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getUpcoming {
            val upcoming = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(upcoming, MovieDbCategory.MovieUpcoming) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieDbCategory.MovieUpcoming) }
                    else { funResponse(moviesAreFavorites(upcoming, listFavorites)) }
                }
            }
        }
    }

    private fun setTime(movieDbCategory: MovieDbCategory) = databaseMovies.lastTimeUpdateCategory(movieDbCategory, getCurrentTime())

    private fun setListDatabaseMovies(movies: List<Movie>, returnRightResponse: (RightResponseMovieCategory) -> Unit, rightResponseMovieCategory: RightResponseMovieCategory) {
        val list: List<Movie> = if(movies.isEmpty() || movies[0].error) listOf() else movies

        if(list.isEmpty())
            return

        setTime(rightResponseMovieCategory.movieDbCategory)

        when(rightResponseMovieCategory.movieDbCategory) {
            MovieDbCategory.MovieNowPlaying -> databaseMovies.setNowPlaying(list, returnRightResponse, rightResponseMovieCategory)
            MovieDbCategory.MoviePopular -> databaseMovies.setPopular(list, returnRightResponse, rightResponseMovieCategory)
            MovieDbCategory.MovieTopRated -> databaseMovies.setTopRated(list, returnRightResponse, rightResponseMovieCategory)
            MovieDbCategory.MovieUpcoming -> databaseMovies.setUpcoming(list, returnRightResponse, rightResponseMovieCategory)
            MovieDbCategory.MovieLatest -> databaseMovies.setLatest(list[0], returnRightResponse, rightResponseMovieCategory)
            else -> Unit
        }
    }


    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun hasNoError(movies: List<Movie>) = movies.isNotEmpty() && !movies.first().error

    private fun isToMakeAPIRequest(movieList: MutableList<Movie>, movieDbCategory: MovieDbCategory, response: (Boolean) -> Unit) {
        databaseMovies.getLastTimeUpdateCategory(movieDbCategory) {
            val deprecated = getCurrentTime().minus(it) > timeToBeDeprecated
            response(movieList.isEmptyOrInLoading() || deprecated)
        }
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

    private fun responseAPI(funResponse: (movies: List<Movie>) -> Unit, favorites: MutableList<Movie>, movieDbCategory: MovieDbCategory) {
        when(movieDbCategory) {
            MovieDbCategory.MovieLatest -> {
                webController.loadLatest {
                    setListDatabaseMovies(listOf(it), this::returnRightResponse,
                        RightResponseMovieCategory(funResponse, listOf(it), movieDbCategory, favorites))
                }
            }
            MovieDbCategory.MovieUpcoming -> {
                webController.loadUpcoming {
                    setListDatabaseMovies(it.results, this::returnRightResponse,
                        RightResponseMovieCategory(funResponse, it.results, movieDbCategory, favorites))
                }
            }
            MovieDbCategory.MovieTopRated -> {
                webController.loadTopRated {
                    setListDatabaseMovies(it.results, this::returnRightResponse,
                        RightResponseMovieCategory(funResponse, it.results, movieDbCategory, favorites))
                }
            }
            MovieDbCategory.MoviePopular -> {
                webController.loadPopular {
                    setListDatabaseMovies(it.results, this::returnRightResponse,
                        RightResponseMovieCategory(funResponse, it.results, movieDbCategory, favorites))
                }
            }
            MovieDbCategory.MovieNowPlaying -> {
                webController.loadNowPlaying {
                    setListDatabaseMovies(it.results, this::returnRightResponse,
                        RightResponseMovieCategory(funResponse, it.results, movieDbCategory, favorites))
                }
            }
            else -> Unit
        }
    }

    private fun returnRightResponse(rightResponseMovieCategory: RightResponseMovieCategory) {

        val movies = rightResponseMovieCategory.movies
        val favorites = rightResponseMovieCategory.favorites
        val funResponse = rightResponseMovieCategory.funResponse
        val movieCategory = rightResponseMovieCategory.movieDbCategory

        val moviesFavorites = moviesAreFavorites(movies, favorites)

        when (movieCategory) {
            MovieDbCategory.MovieLatest -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getLatest { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieDbCategory.MovieUpcoming -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getUpcoming { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieDbCategory.MovieTopRated -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getTopRated { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieDbCategory.MovieNowPlaying -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getNowPlaying { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieDbCategory.MoviePopular -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getPopular { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            else -> Unit
        }
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            block()
        }
    }

}
