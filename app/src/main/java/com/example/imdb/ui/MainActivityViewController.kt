package com.example.imdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.Movie
import java.util.Locale

class MainActivityViewController {

    init {
        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun firstLoadCategory(recycler: RecyclerView, category: MovieCategory) = firstLoadMovies(recycler.movieAdapter, category) {
        recycler.movieAdapter.addMovies(it)
    }

    private fun firstLoadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                                category: MovieCategory,
                                funResponse: (movies: List<Movie>) -> Unit) = when (category) {
        MovieCategory.Latest -> {
            DataController.loadLatest(funResponse)
            adapterMovieList.addMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.NowPlaying -> {
            DataController.loadNowPlaying(funResponse)
            adapterMovieList.addMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.Popular -> {
            DataController.loadPopular(funResponse)
            adapterMovieList.addMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.TopRated -> {
            DataController.loadTopRated(funResponse)
            adapterMovieList.addMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
        MovieCategory.Upcoming -> {
            DataController.loadUpcoming(funResponse)
            adapterMovieList.addMovies(mutableListOf(Movie( 0,"", "", "", true)))
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList

}