package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(
            ForeignKey(entity = TableMoviesList::class,
            parentColumns = arrayOf("idMovieList"),
            childColumns = arrayOf("idMovieList_fk"),
            onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = TableMovie::class,
            parentColumns = arrayOf("idMovie"),
            childColumns = arrayOf("idMovie_fk"),
            onDelete = ForeignKey.CASCADE)
    )
)
data class TableRecommendation(
    @PrimaryKey(autoGenerate = true)
    val idRecommendation: Int,
    val idMovieList_fk: Int,
    val idMovie_fk: Int
)