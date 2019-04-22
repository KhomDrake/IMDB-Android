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
import com.example.imdb.ui.interfaces.IActivityInteraction
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.moviedetail.MovieDetailActivity
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList
import kotlinx.coroutines.withContext
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

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        homeMoviesViewController.favoriteMovie(idMovie, toFavorite)
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieCategory: MovieCategory) {
        this.adapter = createAdapter(iFavorite, movieCategory)
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(iFavorite: IFavorite, movieCategory: MovieCategory)=
        RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieCategory)

    private fun RecyclerView.loadCategory(category: MovieCategory) {
        homeMoviesViewController.loadMovies(this.movieAdapter, category) { this.movieAdapter.setMovies(it) }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
