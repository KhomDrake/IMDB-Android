package com.example.imdb.ui.movies.cast

import com.example.imdb.data.entity.http.movie.MovieCredit

class CastViewController(private val dataController: IDataController) {
    fun loadCast(idMovie: Int, funResponse: (MovieCredit) -> Unit) {
        dataController.loadMovieCredit(idMovie, funResponse)
    }
}