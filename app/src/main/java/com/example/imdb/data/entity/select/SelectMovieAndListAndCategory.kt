package com.example.imdb.data.entity.select

import androidx.room.Embedded
import com.example.imdb.data.entity.table.TableMovie

class SelectMovieAndListAndCategory {
    @Embedded
    lateinit var movie: TableMovie
}