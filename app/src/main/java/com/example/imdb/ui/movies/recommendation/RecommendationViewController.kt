package com.example.imdb.ui.movies.recommendation

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.movie.Movie

class RecommendationViewController(private val repository: Repository) {

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        repository.loadRecommendation(id, funResponse)
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        repository.favoriteMovie(idMovie, toFavorite)
    }

}