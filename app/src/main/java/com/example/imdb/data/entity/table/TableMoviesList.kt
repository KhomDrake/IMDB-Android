package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TableMoviesList(
    @PrimaryKey(autoGenerate = true)
    val idMovieList: Int,
    val page: Int,
    val totalPages: Int
)