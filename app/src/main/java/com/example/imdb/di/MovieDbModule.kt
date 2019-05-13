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
    single {
        Room.databaseBuilder(
            get(), DatabaseMovies::class.java,
            "movie.db"
        ).build()
    }
    single { API(get()) as IWebController }
    single { Repository(get(), get()) as IDataController }
    single { MainActivityViewController(get()) }
    single { HomeAppViewController(get()) }
    single { HomeMoviesViewController(get()) }
    single { MovieDetailViewController(get()) }
    single { RecommendationViewController(get()) }
    single { ReviewViewController(get()) }
    single { CastViewController(get()) }
}