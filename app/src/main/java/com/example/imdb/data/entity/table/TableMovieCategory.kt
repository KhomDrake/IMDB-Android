package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = TableMovie::class,
        parentColumns = arrayOf("idReviewed"),
        childColumns = arrayOf("idMovie_fk"),
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = TableMoviesList::class,
        parentColumns = arrayOf("idMovieList"),
        childColumns = arrayOf("idMovieList_fk"),
        onDelete = ForeignKey.CASCADE)
)
)
data class TableMovieCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idMovie_fk: Int,
    val idMovieList_fk: Int
)