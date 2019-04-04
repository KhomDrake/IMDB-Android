package com.example.imdb.data.entity.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(foreignKeys = arrayOf(
            ForeignKey(entity = TableMovie::class,
            parentColumns = arrayOf("idMovie"),
            childColumns = arrayOf("idMovie_fk"),
            onDelete = ForeignKey.CASCADE)
        )
)
data class TableMovieDetail(
    @PrimaryKey(autoGenerate = true)
    val idMovieDetail: Int,
    val adult: Boolean,
    val idMovie_fk: Int,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val runtime: Int,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int
)