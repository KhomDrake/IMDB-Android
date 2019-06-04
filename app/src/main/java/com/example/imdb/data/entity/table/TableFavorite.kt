package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TableFavorite(
    @PrimaryKey
    val idMovie: Int
)