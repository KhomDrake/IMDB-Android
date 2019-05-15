package com.example.imdb

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.imdb.data.Repository
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.network.API
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.ui.home.HomeAppViewController
import com.example.imdb.ui.mainactivity.MainActivityViewController
import com.example.imdb.ui.movies.cast.CastViewController
import com.example.imdb.ui.movies.homemovies.HomeMoviesActivity
import com.example.imdb.ui.movies.homemovies.HomeMoviesViewController
import com.example.imdb.ui.movies.moviedetail.MovieDetailViewController
import com.example.imdb.ui.movies.moviereview.ReviewViewController
import com.example.imdb.ui.movies.recommendation.RecommendationViewController
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

class HomeMoviesTest : AcceptanceTest<HomeMoviesActivity>(HomeMoviesActivity::class.java) {

    private val databaseMoviesMock: DatabaseMovies = mockk()
    private val apiMock: API = mockk()

    private val movie1 = Movie(
        297802,
        "Aquaman",
        "/5Kg76ldv7VxeX9YlcQXiowHgdX6.jpg",
        "Aquaman",
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )
    private val movie2 = Movie(
        333339,
        "Ready Player One",
        "/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg",
        "Ready Player One",
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )
    private val movie3 = Movie(
        363088,
        "Ant-Man and the Wasp",
        "/eivQmS3wqzqnQWILHLc4FsEfcXP.jpg",
        "Ant-Man and the Wasp",
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )
    private val movie4 = Movie(
        284054,
        "Black Panther",
        "/uxzzxijgPIY7slzFvMotPv8wjKA.jpg",
        "Black Panther",
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )
    private val movie5 = Movie(
        24428,
        "The Avengers",
        "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
        "The Avengers",
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )

    @Before
    fun setup() {

        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieNowPlaying) } returns listOf(movie1, movie2, movie3)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieUpcoming) } returns listOf(movie2, movie4, movie5)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieTopRated) } returns listOf(movie3, movie4, movie5)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieLatest) } returns listOf(movie4, movie3, movie4)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MoviePopular) } returns listOf(movie1, movie3, movie5)
        every { databaseMoviesMock.getFavorites() } returns listOf(movie3)
        every { databaseMoviesMock.getLastTimeUpdateCategory(any()) } returns System.currentTimeMillis()

//        coroutine {
//            every { apiMock.loadCategory(MovieDbCategory.MovieNowPlaying) } returns MoviesList(ZERO, listOf(movie1, movie2, movie3), ZERO)
//            every { apiMock.loadCategory(MovieDbCategory.MovieUpcoming) } returns MoviesList(ZERO, listOf(movie2, movie3, movie5), ZERO)
//            every { apiMock.loadCategory(MovieDbCategory.MovieTopRated) } returns MoviesList(ZERO, listOf(movie1, movie2, movie5), ZERO)
//            every { apiMock.loadCategory(MovieDbCategory.MovieLatest) } returns MoviesList(ZERO, listOf(movie1, movie4, movie3), ZERO)
//            every { apiMock.loadCategory(MovieDbCategory.MoviePopular) } returns MoviesList(ZERO, listOf(movie5, movie2, movie4), ZERO)
//        }

        val moduleMock = listOf(
            module(override = true) {
                single { databaseMoviesMock }
            }
        )

        loadKoinModules(moduleMock)
    }

    @Test
    fun guestIsVisible() {
        Thread.sleep(10000)
        onView(withId(R.id.title_latest)).check(matches(isDisplayed()))
    }

    private fun coroutine(block: suspend () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            block()
        }
    }
}