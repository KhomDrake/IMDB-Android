package com.example.imdb

import com.example.imdb.data.Repository
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.network.API
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class TestTest {

    private lateinit var database: DatabaseMovies
    private lateinit var API: API
    private val movie = Movie(
        ZERO,
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        loading = false,
        error = true,
        adult = false,
        favorite = false
    )

    @Before
    fun `wddas`() {
        clearAllMocks()
        database = mockk()
        API = mockk()
    }

    @Test
    fun `slkdjlaks`() {

        val repositoryMock: Repository = mockk()

        every { repositoryMock.getFavorites(any()) } answers {
            firstArg<(MutableList<Movie>) -> Unit>().invoke(mutableListOf(movie, movie, movie))
        }

        repositoryMock.getFavorites {
            Assert.assertEquals(3, it.count())
        }

    }

}