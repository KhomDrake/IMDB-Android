package com.example.imdb.ui.interfaces

import android.view.View
import com.example.imdb.MovieCategory

interface IActivityInteraction {

    fun loadMovies(type: MovieCategory)

    fun makeImageTransition(view: View, movieId: Int, url: String)
}