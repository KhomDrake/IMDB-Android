package com.example.imdb.ui.home

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie

class HomeAppViewController(private val dataController: DataController) {
    fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        dataController.getFavorites(response)
    }

    fun getPopular(funResponse: (movies: List<Movie>) -> Unit) {
        dataController.loadPopular(funResponse)
    }
}