package com.example.imdb.ui.mainactivity

import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.Movie
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList
import java.util.Locale

class MainActivityViewController {

    private val loading: Movie = Movie( 0,"", "", "", loading = true, error = false)

    init {
        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   category: MovieCategory,
                   funResponse: (movies: List<Movie>) -> Unit) = when (category) {
        MovieCategory.Latest -> {
            adapterMovieList.setMovies(mutableListOf(loading))
            DataController.loadLatest(funResponse)
        }
        MovieCategory.NowPlaying -> {
            adapterMovieList.setMovies(mutableListOf(loading))
            DataController.loadNowPlaying(funResponse)
        }
        MovieCategory.Popular -> {
            adapterMovieList.setMovies(mutableListOf(loading))
            DataController.loadPopular(funResponse)
        }
        MovieCategory.TopRated -> {
            adapterMovieList.setMovies(mutableListOf(loading))
            DataController.loadTopRated(funResponse)
        }
        MovieCategory.Upcoming -> {
            adapterMovieList.setMovies(mutableListOf(loading))
            DataController.loadUpcoming(funResponse)
        }
        else -> Unit
    }

}