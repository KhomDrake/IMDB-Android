package com.example.imdb

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.data.entity.http.movie.MoviesList
import com.example.imdb.network.API
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.ui.ZERO
import com.example.imdb.ui.movies.homemovies.HomeMoviesActivity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

class HomeMoviesTest : AcceptanceTest<HomeMoviesActivity>(HomeMoviesActivity::class.java) {

    private val databaseMoviesMock: DatabaseMovies = mockk(relaxed = true)
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

//        every { databaseMoviesMock.getCategory(any()) } returns listOf(movie1, movie2, movie3)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieNowPlaying) } returns listOf(movie1, movie2, movie3)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieUpcoming) } returns listOf(movie2, movie4, movie5)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieTopRated) } returns listOf(movie3, movie4, movie5)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MovieLatest) } returns listOf(movie4, movie3, movie4)
        every { databaseMoviesMock.getCategory(MovieDbCategory.MoviePopular) } returns listOf(movie1, movie3, movie5)
        every { databaseMoviesMock.setup() } returns Unit
//        every { databaseMoviesMock.setMovie(any(), any()) } returns Unit
        every { databaseMoviesMock.getFavorites() } returns listOf(movie3)
        every { databaseMoviesMock.getLastTimeUpdateCategory(MovieDbCategory.MoviePopular) } returns 0L
        every { databaseMoviesMock.getLastTimeUpdateCategory(MovieDbCategory.MovieLatest) } returns System.currentTimeMillis()
        every { databaseMoviesMock.getLastTimeUpdateCategory(MovieDbCategory.MovieTopRated) } returns 10000000000000L
        every { databaseMoviesMock.getLastTimeUpdateCategory(MovieDbCategory.MovieNowPlaying) } returns System.currentTimeMillis()
        every { databaseMoviesMock.getLastTimeUpdateCategory(MovieDbCategory.MovieUpcoming) } returns System.currentTimeMillis()

//        coEvery { apiMock.loadCategory(MovieDbCategory.MovieNowPlaying) } returns MoviesList(
//            ZERO,
//            listOf(movie1, movie2, movie3),
//            ZERO
//        )
//        coEvery { apiMock.loadCategory(MovieDbCategory.MovieUpcoming) } returns MoviesList(
//            ZERO,
//            listOf(movie2, movie3, movie5),
//            ZERO
//        )
//        coEvery { apiMock.loadCategory(MovieDbCategory.MovieTopRated) } returns MoviesList(
//            ZERO,
//            listOf(movie1, movie2, movie5),
//            ZERO
//        )
//        coEvery { apiMock.loadCategory(MovieDbCategory.MovieLatest) } returns MoviesList(
//            ZERO,
//            listOf(movie1, movie4, movie3),
//            ZERO
//        )
//        coEvery { apiMock.loadCategory(MovieDbCategory.MoviePopular) } returns MoviesList(
//            ZERO,
//            listOf(movie5, movie2, movie4),
//            ZERO
//        )

        val moduleMock = listOf(
            module(override = true) {
                single { databaseMoviesMock }
//                single { apiMock }
            }
        )
        loadKoinModules(moduleMock)
        testRule.launchActivity(Intent())
    }

    @After
    fun tearDown() {
        testRule.finishActivity()
    }

    @Test
    fun guestIsVisible() {
        Thread.sleep(10000)
        onView(withId(R.id.title_latest)).check(matches(isDisplayed()))
    }
}