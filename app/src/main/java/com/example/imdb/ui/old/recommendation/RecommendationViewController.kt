package com.example.imdb.ui.old.recommendation

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie

class RecommendationViewController {

    fun loadRecommendation(id: Int, funResponse: (List<Movie>) -> Unit) {
        DataController.loadRecommendation(id, funResponse)
    }
}