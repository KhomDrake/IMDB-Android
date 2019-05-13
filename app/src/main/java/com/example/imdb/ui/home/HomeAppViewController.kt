package com.example.imdb.ui.home

import com.example.imdb.data.entity.http.movie.Movie

class HomeAppViewController(private val dataController: IDataController) {
    fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        dataController.getFavorites(response)
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        dataController.favoriteMovie(idMovie, toFavorite)
    }
}