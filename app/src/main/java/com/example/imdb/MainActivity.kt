package com.example.imdb

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.data.DataController
import com.example.imdb.data.database.DatabaseMovies
import com.example.imdb.data.entity.table.TableMovie
import com.example.imdb.data.entity.table.TableMovieCategory
import com.example.imdb.data.entity.table.TableMoviesList
import com.example.imdb.ui.mainactivity.MainActivityViewController
import com.example.imdb.ui.mainactivity.RequestCategory
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

enum class MovieCategory {
    Latest,
    NowPlaying,
    Popular,
    TopRated,
    Upcoming,
    Recommendation
}

class MainActivity : AppCompatActivity(), RequestCategory {

    private lateinit var latest: RecyclerView
    private lateinit var nowPlaying: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView

    private lateinit var mainActivityViewController: MainActivityViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewController = MainActivityViewController()

        DataController.createDatabase(this)
        val database = DatabaseMovies.Instance!!
//        database.moviesDao().insertMovieList(TableMoviesList(1, 1, 3, "Latest"))
//        database.moviesDao().insertMovie(TableMovie(23, "asda", "dasas", "asdas", 1))

//        database.moviesDao().insertMovieCategory(TableMovieCategory(1, 23, 1))
        Log.i("Selects", database.moviesDao().getMovies().toString())
        Log.i("Selects", database.moviesDao().getMoviesList().toString())
        Log.i("Selects", database.moviesDao().getMoviesListAndMovie().toString())

        nowPlaying = findViewById(R.id.movies)
        latest = findViewById(R.id.latest)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.toprated)
        upcoming = findViewById(R.id.upcoming)

//        latest.setupAdapter(this, MovieCategory.Latest)
//        nowPlaying.setupAdapter(this, MovieCategory.NowPlaying)
//        popular.setupAdapter(this, MovieCategory.Popular)
//        topRated.setupAdapter(this, MovieCategory.TopRated)
//        upcoming.setupAdapter(this, MovieCategory.Upcoming)
//
//        loadCategory(MovieCategory.Latest)
//        loadCategory(MovieCategory.NowPlaying)
//        loadCategory(MovieCategory.Popular)
//        loadCategory(MovieCategory.TopRated)
//        loadCategory(MovieCategory.Upcoming)
    }

    override fun loadCategory(type: MovieCategory) {
        when(type) {
            MovieCategory.Upcoming -> upcoming.loadCategory(type)
            MovieCategory.NowPlaying -> nowPlaying.loadCategory(type)
            MovieCategory.Popular -> popular.loadCategory(type)
            MovieCategory.TopRated -> topRated.loadCategory(type)
            MovieCategory.Latest -> latest.loadCategory(type)
            else -> Unit
        }
    }

    private fun RecyclerView.setupAdapter(requestCategory: RequestCategory, movieCategory: MovieCategory) {
        this.adapter = createAdapter(requestCategory, movieCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(requestCategory: RequestCategory, movieCategory: MovieCategory) :
            RecyclerViewAdapterMovieList =
        RecyclerViewAdapterMovieList(mutableListOf(), requestCategory, movieCategory)

    private fun RecyclerView.loadCategory(category: MovieCategory) {
        mainActivityViewController.loadMovies(this.movieAdapter, category) {
            this.movieAdapter.setMovies(it)
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
