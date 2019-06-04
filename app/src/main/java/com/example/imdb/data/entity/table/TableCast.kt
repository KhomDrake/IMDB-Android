package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(entity = TableMovie::class,
        parentColumns = arrayOf("idMovie"),
        childColumns = arrayOf("idMovieCredit_fk"),
        onDelete = ForeignKey.CASCADE)
    )
)
data class TableCast(
    @PrimaryKey(autoGenerate = false)
    val idCast: Int,
    val idMovieCredit_fk: Int,
    val character: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val profilePath: String?
)

