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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovieCast(cast: TableCast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetail(movieDetail: TableMovieDetail)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(tableFavorite: TableFavorite)

    @Query("DELETE FROM TableMovie where TableMovie.idReviewed = :idReviewed")
    fun deleteMovie(idMovie: Int)

    @Query("DELETE FROM TableFavorite where TableFavorite.idReviewed = :idReviewed")
    fun deleteFavorite(idMovie: Int)

    @Query("SELECT TableMovie.* FROM TableFavorite, TableMovie where TableFavorite.idReviewed = TableMovie.idReviewed")
    fun getFavorite(): List<TableMovie>

    @Query("DELETE FROM TableMovieCategory where TableMovieCategory.idMovieList_fk = :category")
    fun deleteMovieCategory(category: Int)

    @Query("SELECT * FROM TableMovieDetail where TableMovieDetail.idMovie_fk = :movie")
    fun getMovieDetail(movie: Int): TableMovieDetail?

    @Query("SELECT * FROM TableLastUpdateCategory where TableLastUpdateCategory.idReview = :category")
    fun getLastUpdateCategory(category: Int): TableLastUpdateCategory?

    @Query("SELECT * FROM TableMovie")
    fun getMovies(): List<TableMovie>

    @Query("SELECT * FROM TableMoviesList")
    fun getMoviesList(): List<TableMoviesList>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieRecommendation where TableMovie.idReviewed = TableMovieRecommendation.idMovieRecommendation_fk and TableMovieRecommendation.idMovie_fk = :movieID")
    fun getRecommendationMovie(movieID: Int): List<TableMovie>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieCategory, TableMoviesList where TableMovie.idReviewed = TableMovieCategory.idMovie_fk and TableMovieCategory.idMovieList_fk = :category  and TableMoviesList.idMovieList = :category")
    fun getMoviesListAndMovie(category: Int): List<TableMovie>

    @Query("SELECT TableMovie.*, TableReview.*, TableReviewInformation.* FROM TableMovie, TableReview, TableReviewInformation where TableMovie.idReviewed = :movie and TableReviewInformation.idMovie_fk = :movie and TableReview.idReviewInformation_fk = TableReviewInformation.idReviewInformation")
    fun getMovieReviews(movie: Int): List<SelectMovieReviewsAndReviewInformation>

    @Query("SELECT TableCast.* FROM TableCast where TableCast.idMovieCredit_fk = :movie")
    fun getMovieCreditCast(movie: Int): List<TableCast>

}