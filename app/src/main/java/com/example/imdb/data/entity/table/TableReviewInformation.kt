package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(entity = TableMovie::class,
            parentColumns = arrayOf("idMovie"),
            childColumns = arrayOf("idMovie"),
            onDelete = ForeignKey.CASCADE)
    )
)
data class TableReviewInformation(
    @PrimaryKey(autoGenerate = false)
    val idReviewInformation: Int,
    val idMovie: Int,
    val page: Int,
    val totalPages: Int,
    val totalResults: Int
)