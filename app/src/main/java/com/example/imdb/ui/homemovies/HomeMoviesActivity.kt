package com.example.imdb.ui.homemovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.ActivityInteraction
import com.example.imdb.ui.moviedetail.MovieDetailActivity
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesActivity : AppCompatActivity(), ActivityInteraction {

    private lateinit var latest: RecyclerView
    private lateinit var nowPlaying: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView
    private lateinit var homeMoviesViewController: HomeMoviesViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_movies)

        homeMoviesViewController = HomeMoviesViewController()

        latest = findViewById(R.id.latest)
        nowPlaying = findViewById(R.id.now_playing)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.top_rated)
        upcoming = findViewById(R.id.upcoming)

        latest.setupAdapter(this, MovieCategory.Latest)
        nowPlaying.setupAdapter(this, MovieCategory.NowPlaying)
        popular.setupAdapter(this, MovieCategory.Popular)
        topRated.setupAdapter(this, MovieCategory.TopRated)
        upcoming.setupAdapter(this, MovieCategory.Upcoming)

        loadAllCategorys()
    }

    override fun loadTryAgain(type: MovieCategory) {
        when(type) {
            MovieCategory.Upcoming -> upcoming.loadCategory(type)
            MovieCategory.NowPlaying -> nowPlaying.loadCategory(type)
            MovieCategory.Popular -> popular.loadCategory(type)
            MovieCategory.TopRated -> topRated.loadCategory(type)
            MovieCategory.Latest -> latest.loadCategory(type)
            else -> Unit
        }
    }

    private fun loadAllCategorys() {
        loadTryAgain(MovieCategory.Latest)
        loadTryAgain(MovieCategory.NowPlaying)
        loadTryAgain(MovieCategory.Popular)
        loadTryAgain(MovieCategory.Upcoming)
        loadTryAgain(MovieCategory.TopRated)
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) {
        val startNewActivity = Intent(view.context, MovieDetailActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            view,
            view.transitionName)
        startNewActivity.putExtra("movieID", movieId)
        startNewActivity.putExtra("url", url)
        ContextCompat.startActivity(view.context, startNewActivity, optionsCompat.toBundle())
    }

    override fun updateVisualMovies() {
        loadAllCategorys()
    }

    private fun RecyclerView.setupAdapter(activityInteraction: ActivityInteraction, movieCategory: MovieCategory) {
        this.adapter = createAdapter(activityInteraction, movieCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(activityInteraction: ActivityInteraction, movieCategory: MovieCategory)=
        RecyclerViewAdapterMovieList(mutableListOf(), activityInteraction, movieCategory)

    private fun RecyclerView.loadCategory(category: MovieCategory) {
        homeMoviesViewController.loadMovies(this.movieAdapter, category) { this.movieAdapter.setMovies(it) }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList


}
