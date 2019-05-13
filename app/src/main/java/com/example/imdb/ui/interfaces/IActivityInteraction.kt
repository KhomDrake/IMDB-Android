package com.example.imdb.ui.interfaces

import android.view.View
import com.example.imdb.MovieDbCategory

interface IActivityInteraction {

    fun loadMovies(type: MovieDbCategory)

    fun makeImageTransition(view: View, movieId: Int, url: String)
}