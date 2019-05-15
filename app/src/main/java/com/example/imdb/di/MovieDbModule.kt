package com.example.imdb.di

import androidx.room.Room
import com.example.imdb.data.Repository
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.network.API
import com.example.imdb.ui.movies.cast.CastViewController
import com.example.imdb.ui.home.HomeAppViewController
import com.example.imdb.ui.movies.homemovies.HomeMoviesViewController
import com.example.imdb.ui.mainactivity.MainActivityViewController
import com.example.imdb.ui.movies.moviedetail.MovieDetailViewController
import com.example.imdb.ui.movies.moviereview.ReviewViewController
import com.example.imdb.ui.movies.recommendation.RecommendationViewController
import org.koin.dsl.module.module

val movieDbKoinModule = module {
    single(override = false) {
        Room.databaseBuilder(
            get(), DatabaseMovies::class.java,
            "movie.db"
        ).build()
    }
    single(override = false) { API(get()) }
    single(override = false) { Repository(get(), get()) }
    single(override = false) { MainActivityViewController(get()) }
    single(override = false) { HomeAppViewController(get()) }
    single(override = false) { HomeMoviesViewController(get()) }
    single(override = false) { MovieDetailViewController(get()) }
    single(override = false) { RecommendationViewController(get()) }
    single(override = false) { ReviewViewController(get()) }
    single(override = false) { CastViewController(get()) }
}