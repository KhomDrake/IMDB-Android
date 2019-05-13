package com.example.imdb.ui.movies.homemovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.movies.moviedetail.MovieDetailActivity
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList
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

        latest.setupAdapter(this, MovieDbCategory.MovieLatest)
        nowPlaying.setupAdapter(this, MovieDbCategory.MovieNowPlaying)
        popular.setupAdapter(this, MovieDbCategory.MoviePopular)
        topRated.setupAdapter(this, MovieDbCategory.MovieTopRated)
        upcoming.setupAdapter(this, MovieDbCategory.MovieUpcoming)
    }

    override fun onStart() {
        super.onStart()
        loadAllCategories()
    }

    override fun loadMovies(type: MovieDbCategory) {
        when(type) {
            MovieDbCategory.MovieUpcoming -> upcoming.loadCategory(type)
            MovieDbCategory.MovieNowPlaying -> nowPlaying.loadCategory(type)
            MovieDbCategory.MoviePopular -> popular.loadCategory(type)
            MovieDbCategory.MovieTopRated -> topRated.loadCategory(type)
            MovieDbCategory.MovieLatest -> latest.loadCategory(type)
            else -> Unit
        }
    }

    private fun loadAllCategories() {
        loadMovies(MovieDbCategory.MovieLatest)
        loadMovies(MovieDbCategory.MovieNowPlaying)
        loadMovies(MovieDbCategory.MoviePopular)
        loadMovies(MovieDbCategory.MovieUpcoming)
        loadMovies(MovieDbCategory.MovieTopRated)
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

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        homeMoviesViewController.favoriteMovie(idMovie, toFavorite)
    }

    override fun updateVisualMovie(idMovie: Int, toFavorite: Boolean) {
        val recyclers = listOf(upcoming, nowPlaying, popular, topRated, latest)
        recyclers.forEach {
            val position = it.movieAdapter.getMoviePosition(idMovie)
            it.movieAdapter.favoriteMovie(position, toFavorite)
        }
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieDbCategory: MovieDbCategory) {
        this.adapter = createAdapter(iFavorite, movieDbCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(iFavorite: IFavorite, movieDbCategory: MovieDbCategory)=
        RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieDbCategory)

    private fun RecyclerView.loadCategory(movieDbCategory: MovieDbCategory) {
        homeMoviesViewController.loadMovies(this.movieAdapter, movieDbCategory) {
            this.movieAdapter.setMovies(it)
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
