package com.example.imdb.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.imdb.data.entity.select.SelectMovieReviewsAndReviewInformation
import com.example.imdb.data.entity.table.*

@Dao
interface DaoMovies {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<TableMovie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovieList(movieList: TableMoviesList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: TableMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovieRecommendation(movie: TableMovieRecommendation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastUpdateCategory(updateCategory: TableLastUpdateCategory)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovieCategory(movieCategory: TableMovieCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviewInformation(reviewInformation: TableReviewInformation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(reviews: List<TableReview>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReview(review: TableReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetail(movieDetail: TableMovieDetail)

    @Query("DELETE FROM TableMovieCategory where TableMovieCategory.idMovieList_fk = :category")
    fun deleteMovieCategory(category: Int)

    @Query("SELECT * FROM TableMovieDetail where TableMovieDetail.idMovie_fk = :movie")
    fun getMovieDetail(movie: Int): TableMovieDetail?

    @Query("SELECT * FROM TableLastUpdateCategory where TableLastUpdateCategory.id = :category")
    fun getLastUpdateCategory(category: Int): TableLastUpdateCategory?

    @Query("SELECT * FROM TableMovie")
    fun getMovies(): List<TableMovie>

    @Query("SELECT * FROM TableMoviesList")
    fun getMoviesList(): List<TableMoviesList>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieRecommendation where TableMovie.idMovie = TableMovieRecommendation.idMovieRecommendation_fk and TableMovieRecommendation.idMovie_fk = :movieID")
    fun getRecommendationMovie(movieID: Int): List<TableMovie>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieCategory, TableMoviesList where TableMovie.idMovie = TableMovieCategory.idMovie_fk and TableMovieCategory.idMovieList_fk = :category  and TableMoviesList.idMovieList = :category")
    fun getMoviesListAndMovie(category: Int): List<TableMovie>

    @Query("SELECT TableMovie.*, TableReview.*, TableReviewInformation.* FROM TableMovie, TableReview, TableReviewInformation where TableMovie.idMovie = :movie and TableReviewInformation.idMovie_fk = :movie and TableReview.idReviewInformation_fk = TableReviewInformation.idReviewInformation")
    fun getMovieReviews(movie: Int): List<SelectMovieReviewsAndReviewInformation>

}