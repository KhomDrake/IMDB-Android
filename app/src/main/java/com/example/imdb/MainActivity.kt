package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.data.DataController
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

        DataController.createDatabase(this) {
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
            loadCategory(MovieCategory.Upcoming)
            loadCategory(MovieCategory.TopRated)
        }

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
