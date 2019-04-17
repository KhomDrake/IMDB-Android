package com.example.imdb.ui.homemovies

import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesViewController(private val dataController: DataController) {
    private val loading: Movie =
        Movie(0, "", "", "", loading = true, error = false, adult = false, favorite = false)

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   category: MovieCategory,
                   funResponse: (movies: List<Movie>) -> Unit) {
        adapterMovieList.setMovies(mutableListOf(loading))
        when (category) {
            MovieCategory.Latest -> dataController.loadLatest(funResponse)
            MovieCategory.NowPlaying -> dataController.loadNowPlaying(funResponse)
            MovieCategory.Popular -> dataController.loadPopular(funResponse)
            MovieCategory.TopRated -> dataController.loadTopRated(funResponse)
            MovieCategory.Upcoming -> dataController.loadUpcoming(funResponse)
            else -> Unit
        }
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        dataController.favoriteMovie(idMovie, toFavorite)
    }
}