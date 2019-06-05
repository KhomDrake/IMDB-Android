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
import com.example.imdb.ui.TheMovieDbCategory
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
    private lateinit var recyclersViews: List<RecyclerView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_movies)

        latest = findViewById(R.id.latest)
        nowPlaying = findViewById(R.id.now_playing)
        popular = findViewById(R.id.popular)
        topRated = findViewById(R.id.top_rated)
        upcoming = findViewById(R.id.upcoming)

        recyclersViews = listOf(latest, nowPlaying, popular, topRated, upcoming)

        latest.setupAdapter(this, TheMovieDbCategory.MovieLatest)
        nowPlaying.setupAdapter(this, TheMovieDbCategory.MovieNowPlaying)
        popular.setupAdapter(this, TheMovieDbCategory.MoviePopular)
        topRated.setupAdapter(this, TheMovieDbCategory.MovieTopRated)
        upcoming.setupAdapter(this, TheMovieDbCategory.MovieUpcoming)
        recyclersViews.forEach { it.setupInfiniteScroll() }
    }

    override fun onStart() {
        super.onStart()
        loadAllCategories()
    }

    override fun loadMovies(type: TheMovieDbCategory) {
        when(type) {
            TheMovieDbCategory.MovieUpcoming -> upcoming.loadCategory()
            TheMovieDbCategory.MovieNowPlaying -> nowPlaying.loadCategory()
            TheMovieDbCategory.MoviePopular -> popular.loadCategory()
            TheMovieDbCategory.MovieTopRated -> topRated.loadCategory()
            TheMovieDbCategory.MovieLatest -> latest.loadCategory()
            else -> Unit
        }
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

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) =
        homeMoviesViewController.favoriteMovie(idMovie, toFavorite)

    override fun updateVisualMovie(idMovie: Int, toFavorite: Boolean) {
        recyclersViews.forEach {
            val position = it.movieAdapter.getMoviePosition(idMovie)
            it.movieAdapter.favoriteMovie(position, toFavorite)
        }
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, theMovieDbCategory: TheMovieDbCategory) {
        adapter = createAdapter(iFavorite, theMovieDbCategory)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun createAdapter(iFavorite: IFavorite, theMovieDbCategory: TheMovieDbCategory)=
        RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, theMovieDbCategory)

    private fun loadAllCategories() {
        loadMovies(TheMovieDbCategory.MovieLatest)
        loadMovies(TheMovieDbCategory.MovieNowPlaying)
        loadMovies(TheMovieDbCategory.MoviePopular)
        loadMovies(TheMovieDbCategory.MovieUpcoming)
        loadMovies(TheMovieDbCategory.MovieTopRated)
    }

    private fun RecyclerView.loadCategory() {
        homeMoviesViewController.loadMovies(movieAdapter, movieAdapter.theMovieDbCategory) {
            movieAdapter.setMovies(it)
        }
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList

    private fun RecyclerView.setupInfiniteScroll() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                if (movieAdapter.hasLoading().not() && totalItemCount == lastVisibleItemPosition + 1) {
                    homeMoviesViewController.apply {
                        addPage(movieAdapter.theMovieDbCategory)
                        loadNextMovies(movieAdapter, movieAdapter.theMovieDbCategory) {
                            movieAdapter.addMovies(it)
                        }
                    }
                }
            }
        })
    }

    private val RecyclerView.lastVisibleItemPosition
        get() = linerLayoutManager.findLastVisibleItemPosition()

    private val RecyclerView.linerLayoutManager
        get() = layoutManager as LinearLayoutManager
}
