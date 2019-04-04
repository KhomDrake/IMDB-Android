package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TableMovie(
    @PrimaryKey(autoGenerate = false)
    val idMovie: Int,
    val originalTitle: String,
    val posterPath: String?,
    val title: String,
    val idMovieListConnection: Int
)