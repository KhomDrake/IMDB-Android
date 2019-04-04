package com.example.imdb.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.imdb.data.entity.select.TableMovieAndListAndCategory
import com.example.imdb.data.entity.table.TableMovie
import com.example.imdb.data.entity.table.TableMovieCategory
import com.example.imdb.data.entity.table.TableMoviesList

@Dao
interface DaoMovies {
    @Insert
    fun insertMovies(movies: List<TableMovie>)

    @Insert
    fun insertMovieList(movieList: TableMoviesList)

    @Insert
    fun insertMovie(movie: TableMovie)

    @Insert
    fun insertMovieCategory(movieCategory: TableMovieCategory)

    // insert TableMovieDetail

    // insert TableRecommendation

    // insert TableReview

    // insert TableReviewInformation

    // query SELECT

    @Query("SELECT * FROM TableMovie")
    fun getMovies(): List<TableMovie>

    @Query("SELECT * FROM TableMoviesList")
    fun getMoviesList(): List<TableMoviesList>

    @Query("SELECT TableMovie.*, TableMoviesList.*, TableMovieCategory.* FROM TableMovie INNER JOIN TableMovieCategory ON TableMovie.idMovie = TableMovieCategory.idMovie_fk INNER JOIN TableMoviesList ON TableMovieCategory.idMovieList_fk = TableMoviesList.idMovieList")
    fun getMoviesListAndMovie(): List<TableMovieAndListAndCategory>

    @Query("SELECT TableMovie.*, TableMoviesList.*, TableMovieCategory.* FROM TableMovie INNER JOIN TableMovieCategory ON TableMovie.idMovie = TableMovieCategory.idMovie_fk INNER JOIN TableMoviesList ON TableMovieCategory.idMovieList_fk = :category")
    fun getMoviesListAndMovie(category: Int): List<TableMovieAndListAndCategory>
}