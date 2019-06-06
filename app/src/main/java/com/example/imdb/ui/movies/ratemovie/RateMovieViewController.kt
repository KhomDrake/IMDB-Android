package com.example.imdb.ui.movies.ratemovie

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.Rate

class RateMovieViewController(private val repository: Repository) {
    fun rateMovie(idMovie: Int, body: Rate, response: (String) -> Unit) {
        repository.rateMovie(idMovie, body, response)
    }
}