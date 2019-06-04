package com.example.imdb.data.entity.http

import com.google.gson.annotations.SerializedName

data class Reviews(
    @SerializedName("idReview")
    val idReview: Int,
    val page: Int,
    @SerializedName("results")
    val reviews: List<Review>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    var idReviewed: Int
)