package com.example.imdb.ui.cast

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.MovieCredit

class CastViewController(private val dataController: DataController) {
    fun loadCast(idMovie: Int, funResponse: (MovieCredit) -> Unit) {
        dataController.loadMovieCredit(idMovie, funResponse)
    }
}