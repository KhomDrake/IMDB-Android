package com.example.imdb.ui.movies.moviereview

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.Reviews

class ReviewViewController(private val repository: Repository) {
    fun loadReviews(id: Int, funResponse: (reviews: Reviews) -> Unit) {
        repository.loadReviews(id, funResponse)
    }
}