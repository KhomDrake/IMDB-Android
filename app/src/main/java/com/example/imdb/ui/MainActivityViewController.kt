package com.example.imdb.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.data.DataController
import java.util.Locale

class MainActivityViewController {

    init {
        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun createAdapter(type: MovieCategory): RecyclerViewAdapterMovieList = when (type) {

        MovieCategory.Latest -> RecyclerViewAdapterMovieList(DataController.getLatest())

        MovieCategory.NowPlaying -> RecyclerViewAdapterMovieList(DataController.getNowPlaying())

        MovieCategory.Popular -> RecyclerViewAdapterMovieList(DataController.getPopular())

        MovieCategory.TopRated -> RecyclerViewAdapterMovieList(DataController.getTopRated())

        MovieCategory.Upcoming -> RecyclerViewAdapterMovieList(DataController.getUpcoming())
    }

    fun firstLoadCategory(recycler: RecyclerView, category: MovieCategory) = firstLoadMovies(category) {
        recycler.movieAdapter.notifyDataSetChanged()
    }

    private fun firstLoadMovies(category: MovieCategory, funResponse: () -> Unit) = when (category) {
        MovieCategory.Latest -> DataController.loadLatest(funResponse)
        MovieCategory.NowPlaying -> DataController.loadNowPlaying(funResponse)
        MovieCategory.Popular -> DataController.loadPopular(funResponse)
        MovieCategory.TopRated -> DataController.loadTopRated(funResponse)
        MovieCategory.Upcoming -> DataController.loadUpcoming(funResponse)
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}