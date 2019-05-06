package com.example.imdb.ui.recommendation

import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.Movie

class RecommendationViewController(private val dataController: IDataController) {

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        dataController.loadRecommendation(id, funResponse)
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        dataController.favoriteMovie(idMovie, toFavorite)
    }

}