package com.example.imdb.ui.moviereview

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Reviews

class ReviewViewController(private val dataController: DataController) {
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        dataController.loadReviews(id, funResponse)
    }
}