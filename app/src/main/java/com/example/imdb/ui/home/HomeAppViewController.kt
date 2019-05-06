package com.example.imdb.ui.home

import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.Movie

class HomeAppViewController(private val dataController: IDataController) {
    fun getFavorites(response: (MutableList<Movie>) -> Unit) {
        dataController.getFavorites(response)
    }
}