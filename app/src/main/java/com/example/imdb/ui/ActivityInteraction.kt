package com.example.imdb.ui

import android.view.View
import com.example.imdb.MovieCategory

interface ActivityInteraction {

    fun loadTryAgain(type: MovieCategory)

    fun makeImageTransition(view: View, movieId: Int, url: String)

    fun updateVisualMovies()
}