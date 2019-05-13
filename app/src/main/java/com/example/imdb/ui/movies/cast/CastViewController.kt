package com.example.imdb.ui.movies.cast

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.movie.MovieCredit

class CastViewController(private val repository: Repository) {
    fun loadCast(idMovie: Int, funResponse: (MovieCredit) -> Unit) {
        repository.loadMovieCredit(idMovie, funResponse)
    }
}