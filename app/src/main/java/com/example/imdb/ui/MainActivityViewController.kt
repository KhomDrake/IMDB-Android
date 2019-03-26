package com.example.imdb.ui

import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.Movie
import java.util.Locale

class MainActivityViewController {

    init {
        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   category: MovieCategory,
                   funResponse: (movies: List<Movie>) -> Unit) = when (category) {
        MovieCategory.Latest -> {
            DataController.loadLatest(funResponse)
            adapterMovieList.setMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.NowPlaying -> {
            DataController.loadNowPlaying(funResponse)
            adapterMovieList.setMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.Popular -> {
            DataController.loadPopular(funResponse)
            adapterMovieList.setMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.TopRated -> {
            DataController.loadTopRated(funResponse)
            adapterMovieList.setMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.Upcoming -> {
            DataController.loadUpcoming(funResponse)
            adapterMovieList.setMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
    }

}