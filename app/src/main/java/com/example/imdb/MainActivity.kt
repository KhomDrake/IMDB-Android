package com.example.imdb

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.data.DataController
import com.example.imdb.ui.*

enum class MovieCategory {
    Latest,
    NowPlaying,
    Popular,
    TopRated,
    Upcoming
}

class MainActivity : AppCompatActivity() {

    private lateinit var latest: RecyclerView
    private lateinit var nowPlaying: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView

    private lateinit var mainActivityViewController: MainActivityViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ctx = this

        println("alkdalskjdaslkdjasl")

        mainActivityViewController = MainActivityViewController()

        nowPlaying = findViewById(R.id.movies)
        latest = findViewById(R.id.latest)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.toprated)
        upcoming = findViewById(R.id.upcoming)

        latest.setupAdapter()
        nowPlaying.setupAdapter()
        popular.setupAdapter()
        topRated.setupAdapter()
        upcoming.setupAdapter()

        mainActivityViewController.run {
            latest.loadCategory(MovieCategory.Latest)
            nowPlaying.loadCategory(MovieCategory.NowPlaying)
            popular.loadCategory(MovieCategory.Popular)
            topRated.loadCategory(MovieCategory.TopRated)
            upcoming.loadCategory(MovieCategory.Upcoming)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DataController.restartDatabase()
    }

    private fun RecyclerView.setupAdapter() {
        this.adapter = createAdapter()
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter() = RecyclerViewAdapterMovieList(mutableListOf())

    private fun RecyclerView.loadCategory(category: MovieCategory) {
        mainActivityViewController.loadMovies(this.movieAdapter, category) {
            this.movieAdapter.addMovies(it)
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}

lateinit var ctx: Context