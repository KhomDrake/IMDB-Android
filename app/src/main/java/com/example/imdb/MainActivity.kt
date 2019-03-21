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

        mainActivityViewController = MainActivityViewController()

        nowPlaying = findViewById(R.id.movies)
        latest = findViewById(R.id.latest)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.toprated)
        upcoming = findViewById(R.id.upcoming)

        latest.setupAdapter(MovieCategory.Latest)
        nowPlaying.setupAdapter(MovieCategory.NowPlaying)
        popular.setupAdapter(MovieCategory.Popular)
        topRated.setupAdapter(MovieCategory.TopRated)
        upcoming.setupAdapter(MovieCategory.Upcoming)

        mainActivityViewController.firstLoadCategory(latest, MovieCategory.Latest)
        mainActivityViewController.firstLoadCategory(nowPlaying, MovieCategory.NowPlaying)
        mainActivityViewController.firstLoadCategory(popular, MovieCategory.Popular)
        mainActivityViewController.firstLoadCategory(topRated, MovieCategory.TopRated)
        mainActivityViewController.firstLoadCategory(upcoming, MovieCategory.Upcoming)
    }

    override fun onDestroy() {
        super.onDestroy()
        DataController.restartDatabase()
    }

    private fun RecyclerView.setupAdapter(category: MovieCategory) {
        this.adapter = mainActivityViewController.createAdapter(category)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}