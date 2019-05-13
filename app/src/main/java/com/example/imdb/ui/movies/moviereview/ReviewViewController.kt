package com.example.imdb.ui.movies.moviereview

import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.Reviews

class ReviewViewController(private val dataController: IDataController) {
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        dataController.loadReviews(id, funResponse)
    }
}