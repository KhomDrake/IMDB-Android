package com.example.imdb.ui.homemovies

import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesViewController {
    private val loading: Movie =
        Movie(0, "", "", "", loading = true, error = false, adult = false, favorite = false)

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   category: MovieCategory,
                   funResponse: (movies: List<Movie>) -> Unit) {
        adapterMovieList.setMovies(mutableListOf(loading))
        when (category) {
            MovieCategory.Latest -> DataController.loadLatest(funResponse)
            MovieCategory.NowPlaying -> DataController.loadNowPlaying(funResponse)
            MovieCategory.Popular -> DataController.loadPopular(funResponse)
            MovieCategory.TopRated -> DataController.loadTopRated(funResponse)
            MovieCategory.Upcoming -> DataController.loadUpcoming(funResponse)
            else -> Unit
        }
    }
}