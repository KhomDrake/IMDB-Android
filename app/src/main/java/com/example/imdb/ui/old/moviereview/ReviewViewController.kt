package com.example.imdb.ui.old.moviereview

import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Reviews

class ReviewViewController {
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        DataController.loadReviews(id, funResponse)
    }
}