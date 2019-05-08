package com.example.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.ui.homemovies.HomeMoviesActivity
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.startKoin

class HomeMoviesTest : AcceptanceTest<HomeMoviesActivity>(HomeMoviesActivity::class.java) {

    val dataControllerMock: DataController = mockk()

    private val movie1 = Movie(297802, "Aquaman", "/5Kg76ldv7VxeX9YlcQXiowHgdX6.jpg", "Aquaman", loading = false, error = true, adult = false, favorite = false)
    private val movie2 = Movie(333339, "Ready Player One", "/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg", "Ready Player One", loading = false, error = true, adult = false, favorite = false)
    private val movie3 = Movie(363088, "Ant-Man and the Wasp", "/eivQmS3wqzqnQWILHLc4FsEfcXP.jpg", "Ant-Man and the Wasp", loading = false, error = true, adult = false, favorite = false)
    private val movie4 = Movie(284054, "Black Panther", "/uxzzxijgPIY7slzFvMotPv8wjKA.jpg", "Black Panther", loading = false, error = true, adult = false, favorite = false)
    private val movie5 = Movie(24428, "The Avengers", "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg", "The Avengers", loading = false, error = true, adult = false, favorite = false)

    @Before
    fun setup() {
        startKoin(
            listOf(module {
                single { dataControllerMock }
            })
        )

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
        dataControllerMock.loadLatest {
            println(it)
        }
    }

    @Test
    fun guestIsVisible() {

        Thread.sleep(5000)
        onView(withId(R.id.title_latest)).check(matches(isDisplayed()))
    }
}