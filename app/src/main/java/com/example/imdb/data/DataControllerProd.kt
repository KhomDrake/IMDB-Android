package com.example.imdb.data

import com.example.imdb.MovieCategory
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.*
import com.example.imdb.network.WebController

class DataControllerProd(private val webController: WebController, private val databaseMovies: DatabaseMovies) : DataController {

    private lateinit var language: String

    private val timeToBeDeprecated: Long = 5 * 60000

    override fun setupDatabase(language: String) { this.language = language }

    override fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        databaseMovies.getFavorites(response)
    }

    override fun favoriteMovie(movieId: Int, toFavorite: Boolean) = databaseMovies.favoriteMovie(movieId, toFavorite)

    override fun loadMovieCredit(id: Int, funResponse: (movieCredit: MovieCredit) -> Unit) {
        databaseMovies.getMovieCredit(id) {
            val movieCredit = it
            if(id != movieCredit.id) {
                webController.loadMovieCredit(id) {
                    databaseMovies.setCreditMovie(it, id)
                    funResponse(it)
                }
            } else { funResponse(movieCredit) }
        }
    }

    override fun loadMovieDetail(id: Int, funResponse: (movies: MovieDetail) -> Unit) {
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

    override fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
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

    override fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        databaseMovies.getRecommendationLastMovie(id) {
            val recommendation = it
            databaseMovies.getFavorites {
                val listFavorites = it
                if(recommendation.id != id) {
                    webController.loadRecommendation(id) {
                        val moviesList = it
                        if(moviesList.results.isNotEmpty() && !moviesList.results.first().error) {
                            databaseMovies.setRecommendationLastMovie(Recommendation(id, moviesList))
                        }
                        funResponse(moviesAreFavorites(moviesList.results, listFavorites))
                    }
                } else { funResponse(moviesAreFavorites(recommendation.moviesList.results, listFavorites)) }
            }
        }
    }

    override fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getLatest {
            val latest = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(latest, MovieCategory.Latest) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieCategory.Latest) }
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
                isToMakeAPIRequest(nowPlaying, MovieCategory.NowPlaying) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieCategory.NowPlaying) }
                    else { funResponse(nowPlaying) }
                }
            }
        }
    }

    override fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getPopular {
            val popular = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(popular, MovieCategory.Popular) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieCategory.Popular) }
                    else { funResponse(popular) }
                }
            }
        }
    }

    override fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getTopRated {
            val topRated = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(topRated, MovieCategory.TopRated) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieCategory.TopRated) }
                    else { funResponse(topRated) }
                }
            }
        }
    }

    override fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getUpcoming {
            val upcoming = it
            databaseMovies.getFavorites {
                val listFavorites = it
                isToMakeAPIRequest(upcoming, MovieCategory.Upcoming) {
                    val makeRequest = it
                    if (makeRequest) { responseAPI(funResponse, listFavorites, MovieCategory.Upcoming) }
                    else { funResponse(upcoming) }
                }
            }
        }
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


    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun hasNoError(movies: List<Movie>) = movies.isNotEmpty() && !movies.first().error

    private fun isToMakeAPIRequest(movieList: MutableList<Movie>, movieCategory: MovieCategory, response: (Boolean) -> Unit) {
        databaseMovies.getLastTimeUpdateCategory(movieCategory) {
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

    private fun responseAPI(funResponse: (movies: List<Movie>) -> Unit, favorites: MutableList<Movie>, movieCategory: MovieCategory) {
        when(movieCategory) {
            MovieCategory.Latest -> {
                webController.loadLatest {
                    setListDatabaseMovies(listOf(it), movieCategory)
                    returnRightResponse(funResponse, listOf(it), movieCategory, favorites)
                }
            }
            MovieCategory.Upcoming -> {
                webController.loadUpcoming {
                    setListDatabaseMovies(it.results, movieCategory)
                    returnRightResponse(funResponse, it.results, movieCategory, favorites)
                }
            }
            MovieCategory.TopRated -> {
                webController.loadTopRated {
                    setListDatabaseMovies(it.results, movieCategory)
                    returnRightResponse(funResponse, it.results, movieCategory, favorites)
                }
            }
            MovieCategory.Popular -> {
                webController.loadPopular {
                    setListDatabaseMovies(it.results, movieCategory)
                    returnRightResponse(funResponse, it.results, movieCategory, favorites)
                }
            }
            MovieCategory.NowPlaying -> {
                webController.loadNowPlaying {
                    setListDatabaseMovies(it.results, movieCategory)
                    returnRightResponse(funResponse, it.results, movieCategory, favorites)
                }
            }
            else -> Unit
        }
    }

    private fun returnRightResponse(funResponse: (movies: List<Movie>) -> Unit,
                                    movies: List<Movie>,
                                    movieCategory: MovieCategory, favorites: MutableList<Movie>) {

        val moviesFavorites = moviesAreFavorites(movies, favorites)

        when (movieCategory) {
            MovieCategory.Latest -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getLatest { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieCategory.Upcoming -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getUpcoming { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieCategory.TopRated -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getTopRated { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieCategory.NowPlaying -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getNowPlaying { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            MovieCategory.Popular -> if (hasNoError(moviesFavorites)) {
                databaseMovies.getPopular { funResponse(moviesAreFavorites(it, favorites)) }
            } else funResponse(moviesFavorites)

            else -> Unit
        }
    }

}
