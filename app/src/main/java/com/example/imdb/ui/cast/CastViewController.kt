package com.example.imdb.ui.cast

import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.MovieCredit

class CastViewController(private val dataController: IDataController) {
    fun loadCast(idMovie: Int, funResponse: (MovieCredit) -> Unit) {
        dataController.loadMovieCredit(idMovie, funResponse)
    }
}