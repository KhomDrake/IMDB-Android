package com.example.imdb.ui.home

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.movie.Movie

class HomeAppViewController(private val repository: Repository) {
    fun getFavorites(response: (List<Movie>) -> Unit) {
        repository.getFavorites(response)
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        repository.favoriteMovie(idMovie, toFavorite)
    }
}