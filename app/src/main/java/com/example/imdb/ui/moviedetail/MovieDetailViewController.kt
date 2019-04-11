package com.example.imdb.ui.moviedetail

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.MovieDetail

class MovieDetailViewController {

    fun loadMovieDetail(id: Int, funResponse: (MovieDetail) -> Unit) {
        DataController.loadMovieDetail(id, funResponse)
    }

    fun loadMovieCast(id: Int, funResponse: (MovieDetail) -> Unit) {

    }
}