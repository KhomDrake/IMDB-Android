package com.example.imdb

import com.example.imdb.auxiliary.EMPTY_STRING
import com.example.imdb.auxiliary.ZERO
import com.example.imdb.data.DataController
import com.example.imdb.data.IDataController
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.Movie
import com.example.imdb.network.WebController
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestTest {

    private lateinit var database: DatabaseMovies
    private lateinit var webController: WebController
    private val movie = Movie(ZERO, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, loading = false, error = true, adult = false, favorite = false)

    @Before
    fun `wddas`() {
        clearAllMocks()
        database = mockk()
        webController = mockk()
    }

    @Test
    fun `slkdjlaks`() {

        val dataControllerMock: DataController = mockk()

        every { dataControllerMock.getFavorites(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie, movie, movie))
        }

        dataControllerMock.getFavorites {
            Assert.assertEquals(3, it.count())
        }

    }

}