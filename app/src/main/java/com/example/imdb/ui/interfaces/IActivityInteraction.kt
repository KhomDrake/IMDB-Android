package com.example.imdb.ui.interfaces

import android.view.View
import com.example.imdb.ui.TheMovieDbCategory

interface IActivityInteraction {

    fun loadMovies(type: TheMovieDbCategory)

    fun makeImageTransition(view: View, movieId: Int, url: String)
}