package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.imdb.MovieCategory

@Entity
data class TableMoviesList(
    @PrimaryKey(autoGenerate = true)
    val idMovieList: Int,
    val page: Int,
    val totalPages: Int,
    val category: String
)