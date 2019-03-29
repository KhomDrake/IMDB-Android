package com.example.imdb

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.ui.*

enum class MovieCategory {
    Latest,
    NowPlaying,
    Popular,
    TopRated,
    Upcoming
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
        ctx = this

        mainActivityViewController = MainActivityViewController()

        nowPlaying = findViewById(R.id.movies)
        latest = findViewById(R.id.latest)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.toprated)
        upcoming = findViewById(R.id.upcoming)

        latest.setupAdapter(this, MovieCategory.Latest)
        nowPlaying.setupAdapter(this, MovieCategory.NowPlaying)
        popular.setupAdapter(this, MovieCategory.Popular)
        topRated.setupAdapter(this, MovieCategory.TopRated)
        upcoming.setupAdapter(this, MovieCategory.Upcoming)

        loadCategory(MovieCategory.Latest)
        loadCategory(MovieCategory.NowPlaying)
        loadCategory(MovieCategory.Popular)
        loadCategory(MovieCategory.TopRated)
        loadCategory(MovieCategory.Upcoming)
    }

    override fun loadCategory(type: MovieCategory) {
        when(type) {
            MovieCategory.Upcoming -> upcoming.loadCategory(type)
            MovieCategory.NowPlaying -> nowPlaying.loadCategory(type)
            MovieCategory.Popular -> popular.loadCategory(type)
            MovieCategory.TopRated -> topRated.loadCategory(type)
            MovieCategory.Latest -> latest.loadCategory(type)
        }
    }

    private fun RecyclerView.setupAdapter(requestCategory: RequestCategory, movieCategory: MovieCategory) {
        this.adapter = createAdapter(requestCategory, movieCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(requestCategory: RequestCategory, movieCategory: MovieCategory) :
            RecyclerViewAdapterMovieList = RecyclerViewAdapterMovieList(mutableListOf(), requestCategory, movieCategory)

    private fun RecyclerView.loadCategory(category: MovieCategory) {
        mainActivityViewController.loadMovies(this.movieAdapter, category) {
            println(it)
            this.movieAdapter.setMovies(it)
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}

@SuppressLint("StaticFieldLeak")
lateinit var ctx: Context