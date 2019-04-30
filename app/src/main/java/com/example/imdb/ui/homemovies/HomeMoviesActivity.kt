package com.example.imdb.ui.homemovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.moviedetail.MovieDetailActivity
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList
import org.koin.android.ext.android.inject

class HomeMoviesActivity : AppCompatActivity(), IFavorite {

    private lateinit var latest: RecyclerView
    private lateinit var nowPlaying: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView
    private val homeMoviesViewController: HomeMoviesViewController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_movies)

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

        loadAllCategories()
    }

    override fun loadMovies(type: MovieCategory) {
        when(type) {
            MovieCategory.Upcoming -> upcoming.loadCategory(type, this)
            MovieCategory.NowPlaying -> nowPlaying.loadCategory(type, this)
            MovieCategory.Popular -> popular.loadCategory(type, this)
            MovieCategory.TopRated -> topRated.loadCategory(type, this)
            MovieCategory.Latest -> latest.loadCategory(type, this)
            else -> Unit
        }
    }

    private fun loadAllCategories() {
        loadMovies(MovieCategory.Latest)
        loadMovies(MovieCategory.NowPlaying)
        loadMovies(MovieCategory.Popular)
        loadMovies(MovieCategory.Upcoming)
        loadMovies(MovieCategory.TopRated)
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
        loadAllCategories()
    }

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        homeMoviesViewController.favoriteMovie(idMovie, toFavorite)
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieCategory: MovieCategory) {
        this.adapter = createAdapter(iFavorite, movieCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(iFavorite: IFavorite, movieCategory: MovieCategory)=
        RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieCategory)

    private fun RecyclerView.loadCategory(category: MovieCategory, appCompatActivity: AppCompatActivity) {
        appCompatActivity.runOnUiThread {
            homeMoviesViewController.loadMovies(this.movieAdapter, category) { this.movieAdapter.setMovies(it) }
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
