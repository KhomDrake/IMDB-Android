package com.example.imdb.data.entity.select

import androidx.room.Embedded
import com.example.imdb.data.entity.table.TableMovie
import com.example.imdb.data.entity.table.TableMovieCategory
import com.example.imdb.data.entity.table.TableMoviesList

class TableMovieAndListAndCategory {
    @Embedded
    lateinit var movie: TableMovie

    @Embedded
    lateinit var movieList: TableMoviesList

    @Embedded
    lateinit var movieCategory: TableMovieCategory

    @Override
    override fun toString(): String {
        return "${movie.idMovie} ${movieList.idMovieList} ${movieCategory.id}"
    }
}