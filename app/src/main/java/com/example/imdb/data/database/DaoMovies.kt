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
    suspend fun insertMovies(movies: List<TableMovie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieList(movieList: TableMoviesList)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: TableMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieRecommendation(movie: TableMovieRecommendation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastUpdateCategory(updateCategory: TableLastUpdateCategory)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieCategory(movieCategory: TableMovieCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviewInformation(reviewInformation: TableReviewInformation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<TableReview>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReview(review: TableReview)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieCast(cast: TableCast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: TableMovieDetail)

    @Query("UPDATE TableMovie SET favorite = :favorite where idMovie = :movieId")
    suspend fun favoriteMovie(movieId: Int, favorite: Boolean)

    @Query("SELECT * FROM TableMovie where TableMovie.favorite = :favorite")
    suspend fun getFavorite(favorite: Boolean = true): List<TableMovie>

    @Query("DELETE FROM TableMovieCategory where TableMovieCategory.idMovieList_fk = :category")
    suspend fun deleteMovieCategory(category: Int)

    @Query("SELECT * FROM TableMovieDetail where TableMovieDetail.idMovie_fk = :movie")
    suspend fun getMovieDetail(movie: Int): TableMovieDetail?

    @Query("SELECT * FROM TableLastUpdateCategory where TableLastUpdateCategory.id = :category")
    suspend fun getLastUpdateCategory(category: Int): TableLastUpdateCategory?

    @Query("SELECT * FROM TableMovie")
    suspend fun getMovies(): List<TableMovie>

    @Query("SELECT * FROM TableMoviesList")
    suspend fun getMoviesList(): List<TableMoviesList>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieRecommendation where TableMovie.idMovie = TableMovieRecommendation.idMovieRecommendation_fk and TableMovieRecommendation.idMovie_fk = :movieID")
    suspend fun getRecommendationMovie(movieID: Int): List<TableMovie>

    @Query("SELECT TableMovie.* FROM TableMovie, TableMovieCategory, TableMoviesList where TableMovie.idMovie = TableMovieCategory.idMovie_fk and TableMovieCategory.idMovieList_fk = :category  and TableMoviesList.idMovieList = :category")
    suspend fun getMoviesListAndMovie(category: Int): List<TableMovie>

    @Query("SELECT TableMovie.*, TableReview.*, TableReviewInformation.* FROM TableMovie, TableReview, TableReviewInformation where TableMovie.idMovie = :movie and TableReviewInformation.idMovie_fk = :movie and TableReview.idReviewInformation_fk = TableReviewInformation.idReviewInformation")
    suspend fun getMovieReviews(movie: Int): List<SelectMovieReviewsAndReviewInformation>

    @Query("SELECT TableCast.* FROM TableCast where TableCast.idMovieCredit_fk = :movie")
    suspend fun getMovieCreditCast(movie: Int): List<TableCast>

}