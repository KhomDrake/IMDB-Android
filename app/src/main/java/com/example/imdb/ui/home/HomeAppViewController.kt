package com.example.imdb.ui.home

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie

class HomeAppViewController {
    fun getFavorites(): List<Movie> = DataController.getFavorites()

    fun getPopular(funResponse: (movies: List<Movie>) -> Unit) {
        DataController.loadPopular(funResponse)
    }
}