package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = TableMovie::class,
        parentColumns = arrayOf("idReviewed"),
        childColumns = arrayOf("idMovie_fk"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = TableMovie::class,
        parentColumns = arrayOf("idReviewed"),
        childColumns = arrayOf("idMovieRecommendation_fk"),
        onDelete = ForeignKey.CASCADE)))
data class TableMovieRecommendation(
    @PrimaryKey(autoGenerate = false)
    val idRecommendation: Int,
    val idMovie_fk: Int,
    val idMovieRecommendation_fk: Int
)