package com.example.imdb.ui.interfaces

interface IFavorite : IActivityInteraction {

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean)

    fun updateVisualMovie(idMovie: Int, toFavorite: Boolean)
}