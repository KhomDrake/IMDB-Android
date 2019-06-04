package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(entity = TableReviewInformation::class,
            parentColumns = arrayOf("idReviewInformation"),
            childColumns = arrayOf("idReviewInformation_fk"),
            onDelete = ForeignKey.CASCADE)
    )
)
data class TableReview(
    @PrimaryKey(autoGenerate = true)
    val idReview: Int,
    val author: String,
    val content: String,
    val url: String,
    val idReviewInformation_fk: Int
)