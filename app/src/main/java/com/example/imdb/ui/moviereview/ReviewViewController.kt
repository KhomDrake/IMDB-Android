package com.example.imdb.ui.moviereview

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.Reviews

class ReviewViewController {
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        DataController.loadReviews(id, funResponse)
    }
}