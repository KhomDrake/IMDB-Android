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

    override fun favoriteMovie(movieId: Int, favorite: Boolean) = databaseMovies.favoriteMovie(movieId, favorite)

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
            if(recommendation.id != id) {
                webController.loadRecommendation(id) {
                    if(it.results.isNotEmpty() && !it.results.first().error)
                        databaseMovies.setRecommendationLastMovie(Recommendation(id, it))
                    databaseMovies.getRecommendationLastMovie(id) {
                        funResponse(it.moviesList.results)
                    }
                }
            } else { funResponse(recommendation.moviesList.results) }
        }
    }

    override fun loadLatest(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getLatest {
            val latest = it
            isToMakeNewAPICall(latest, MovieCategory.Latest) {
                val makeCall = it
                if (makeCall) {
                    webController.loadLatest {
                        setListDatabaseMovies(listOf(it), MovieCategory.Latest)
                        returnRightResponse(funResponse, listOf(it), MovieCategory.Latest)
                    }
                } else { funResponse(latest) }
            }
        }
    }

    override fun loadNowPlaying(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getNowPlaying {
            val nowPlaying = it
            isToMakeNewAPICall(nowPlaying, MovieCategory.NowPlaying) {
                val makeCall = it
                if (makeCall) {
                    webController.loadNowPlaying {
                        setListDatabaseMovies(it.results, MovieCategory.NowPlaying)
                        returnRightResponse(funResponse, it.results, MovieCategory.NowPlaying)
                    }
                } else { funResponse(nowPlaying) }
            }
        }
    }

    override fun loadPopular(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getPopular {
            val popular = it
            isToMakeNewAPICall(popular, MovieCategory.Popular) {
                val makeCall = it
                if (makeCall) {
                    webController.loadPopular {
                        setListDatabaseMovies(it.results, MovieCategory.Popular)
                        returnRightResponse(funResponse, it.results, MovieCategory.Popular)
                    }
                } else { funResponse(popular) }
            }
        }
    }

    override fun loadTopRated(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getTopRated {
            val topRated = it
            isToMakeNewAPICall(topRated, MovieCategory.TopRated) {
                val makeCall = it
                if (makeCall) {
                    webController.loadTopRated {
                        setListDatabaseMovies(it.results, MovieCategory.TopRated)
                        returnRightResponse(funResponse, it.results, MovieCategory.TopRated)
                    }
                } else { funResponse(topRated) }
            }
        }
    }

    override fun loadUpcoming(funResponse: (movies: List<Movie>) -> Unit) {
        databaseMovies.getUpcoming {
            val upcoming = it
            isToMakeNewAPICall(upcoming, MovieCategory.Upcoming) {
                val makeCall = it
                if (makeCall) {
                    webController.loadUpcoming {
                        setListDatabaseMovies(it.results, MovieCategory.Upcoming)
                        returnRightResponse(funResponse, it.results, MovieCategory.Upcoming)
                    }
                } else { funResponse(upcoming) }
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

    private fun returnRightResponse(funResponse: (movies: List<Movie>) -> Unit,
                                    movies: List<Movie>,
                                    movieCategory: MovieCategory) {
        when (movieCategory) {
            MovieCategory.Latest -> if (hasNoError(movies)) { databaseMovies.getLatest { funResponse(it) } } else funResponse(movies)
            MovieCategory.Upcoming -> if (hasNoError(movies)) { databaseMovies.getUpcoming { funResponse(it) } } else funResponse(movies)
            MovieCategory.TopRated -> if (hasNoError(movies)) { databaseMovies.getTopRated { funResponse(it) } } else funResponse(movies)
            MovieCategory.NowPlaying -> if (hasNoError(movies)) { databaseMovies.getNowPlaying { funResponse(it) } } else funResponse(movies)
            MovieCategory.Popular -> if (hasNoError(movies)) { databaseMovies.getPopular { funResponse(it) } } else funResponse(movies)
            else -> Unit
        }
    }


    private fun MutableList<Movie>.isEmptyOrInLoading(): Boolean {
        for (movie in this) if (movie.loading) return true

        if (this.count() == 0) return true

        return false
    }

    private fun hasNoError(movies: List<Movie>) = movies.isNotEmpty() && !movies.first().error

    private fun isToMakeNewAPICall(movieList: MutableList<Movie>, movieCategory: MovieCategory, response: (Boolean) -> Unit) {
        databaseMovies.getLastTimeUpdateCategory(movieCategory) {
            val deprecated = getCurrentTime().minus(it) > timeToBeDeprecated
            response(movieList.isEmptyOrInLoading() || deprecated)
        }
    }

    private fun getCurrentTime() = System.currentTimeMillis()

}
