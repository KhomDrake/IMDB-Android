package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TableLastUpdateCategory(
    @PrimaryKey
    val id: Int,
    val timeUpdate: Long
)