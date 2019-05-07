package com.example.imdb.di

import androidx.room.Room
import com.example.imdb.data.IDataController
import com.example.imdb.data.DataController
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.database.IDatabaseMovies
import com.example.imdb.network.IWebController
import com.example.imdb.network.WebController
import com.example.imdb.ui.cast.CastViewController
import com.example.imdb.ui.home.HomeAppViewController
import com.example.imdb.ui.homemovies.HomeMoviesViewController
import com.example.imdb.ui.mainactivity.MainActivityViewController
import com.example.imdb.ui.moviedetail.MovieDetailViewController
import com.example.imdb.ui.moviereview.ReviewViewController
import com.example.imdb.ui.recommendation.RecommendationViewController
import org.koin.dsl.module.module

val movieDbKoinModule = module {
    single {
        Room.databaseBuilder(
            get(), DatabaseMovies::class.java,
            "movie.db"
        ).build() as IDatabaseMovies
    }
    single { WebController(get()) as IWebController }
    single { DataController(get(), get()) as IDataController }
    single { MainActivityViewController(get()) }
    single { HomeAppViewController(get()) }
    single { HomeMoviesViewController(get()) }
    single { MovieDetailViewController(get()) }
    single { RecommendationViewController(get()) }
    single { ReviewViewController(get()) }
    single { CastViewController(get()) }
}