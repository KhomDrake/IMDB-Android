package com.example.imdb.di

import androidx.room.Room
import com.example.imdb.data.Repository
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.network.API
import com.example.imdb.ui.movies.cast.CastViewController
import com.example.imdb.ui.home.HomeAppViewController
import com.example.imdb.ui.login.LoginActivityViewController
import com.example.imdb.ui.movies.homemovies.HomeMoviesViewController
import com.example.imdb.ui.mainactivity.MainActivityViewController
import com.example.imdb.ui.movies.castdetail.CastDetailViewController
import com.example.imdb.ui.movies.moviedetail.MovieDetailViewController
import com.example.imdb.ui.movies.moviereview.ReviewViewController
import com.example.imdb.ui.movies.ratemovie.RateMovieViewController
import com.example.imdb.ui.movies.recommendation.RecommendationViewController
import org.koin.dsl.module.module

val movieDbKoinModule = module(override = false) {
    single {
        Room.databaseBuilder(
            get(), DatabaseMovies::class.java,
            "movie.db"
        ).build()
    }
    single { API(get()) }
    single { Repository(get(), get()) }
    single { MainActivityViewController(get()) }
    single { HomeAppViewController(get()) }
    single { HomeMoviesViewController(get()) }
    single { MovieDetailViewController(get()) }
    single { RecommendationViewController(get()) }
    single { ReviewViewController(get()) }
    single { RateMovieViewController(get()) }
    single { CastViewController(get()) }
    single { LoginActivityViewController(get()) }
    single { CastDetailViewController(get()) }
}