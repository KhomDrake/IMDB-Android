package com.example.imdb.ui.cast

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.MovieCredit

class CastViewController {
    fun loadCast(idMovie: Int, funResponse: (MovieCredit) -> Unit) {
        DataController.loadMovieCredit(idMovie, funResponse)
    }
}