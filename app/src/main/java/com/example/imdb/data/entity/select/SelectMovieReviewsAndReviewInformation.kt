package com.example.imdb.data.entity.select

import androidx.room.Embedded
import com.example.imdb.data.entity.table.*

class SelectMovieReviewsAndReviewInformation {

    @Embedded
    lateinit var movie: TableMovie

    @Embedded
    lateinit var review: TableReview

    @Embedded
    lateinit var reviewInformation: TableReviewInformation
}