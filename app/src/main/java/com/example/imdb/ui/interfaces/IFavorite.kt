package com.example.imdb.ui.interfaces

interface IFavorite : IActivityInteraction {

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean)
}