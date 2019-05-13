package com.example.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.imdb.data.IDataController
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.ui.movies.homemovies.HomeMoviesActivity
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class HomeMoviesTest : AcceptanceTest<HomeMoviesActivity>(HomeMoviesActivity::class.java) {

    private val databaseMovies: DatabaseMovies = mockk()
    private val dataControllerMock: IDataController = mockk()

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
        every { databaseMovies.getLatest(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie3))
        }
        every { databaseMovies.getNowPlaying(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie1, movie2, movie3))
        }
        every { databaseMovies.getPopular(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie3, movie4, movie5))
        }
        every { databaseMovies.getTopRated(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie2, movie3, movie5))
        }
        every { databaseMovies.getUpcoming(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie2, movie4, movie1))
        }
        every { databaseMovies.getFavorites(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie2, movie5))
        }
        every { databaseMovies.getLastTimeUpdateCategory(any(), any()) } answers {
            secondArg<(Long) -> Unit>().invoke(System.currentTimeMillis())
        }

        every { dataControllerMock.loadLatest(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie3))
        }
        every { dataControllerMock.loadNowPlaying(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie1, movie2, movie3))
        }
        every { dataControllerMock.loadPopular(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie3, movie4, movie5))
        }
        every { dataControllerMock.loadTopRated(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie2, movie3, movie5))
        }
        every { dataControllerMock.loadUpcoming(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie2, movie4, movie1))
        }

//        startKoin(
//            listOf(
//                module(override = true) {
//                    single { databaseMovies }
//                    single { WebController(get()) as IWebController }
//                    single { dataControllerMock }
//                    single { MainActivityViewController(get()) }
//                    single { HomeAppViewController(get()) }
//                    single { HomeMoviesViewController(get()) }
//                    single { MovieDetailViewController(get()) }
//                    single { RecommendationViewController(get()) }
//                    single { ReviewViewController(get()) }
//                    single { CastViewController(get()) }
//                }
//            )
//        )
    }

    @Test
    fun guestIsVisible() {
        Thread.sleep(10000)
        onView(withId(R.id.title_latest)).check(matches(isDisplayed()))
    }
}