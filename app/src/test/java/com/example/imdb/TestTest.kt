package com.example.imdb

import com.example.imdb.data.DataController
import com.example.imdb.data.IDataController
import com.example.imdb.data.database.DatabaseMovies
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestTest {

    private lateinit var database: DatabaseMovies

    @Before
    fun `wddas`() {
        database = mockk()
    }

    @Test
    fun `slkdjlaks`() {

        val dataControllerMock: IDataController = DataControllerImplTest()

        dataControllerMock.getFavorites {
            Assert.assertEquals(3, it.count())
        }

    }

}