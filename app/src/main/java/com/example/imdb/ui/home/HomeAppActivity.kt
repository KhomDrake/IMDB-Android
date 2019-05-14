package com.example.imdb.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.R
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.ui.becomeVisibleOrInvisible
import com.example.imdb.ui.movies.homemovies.HomeMoviesActivity
import com.example.imdb.ui.tv.hometv.HomeTvActivity
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.movies.moviedetail.MovieDetailActivity
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList
import org.koin.android.ext.android.inject

class HomeAppActivity : AppCompatActivity(), IFavorite {

    private lateinit var movie: Button
    private lateinit var tv: Button
    private lateinit var messengerNotFavorites: TextView
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var loadingFavorite: ProgressBar
    private val homeAppViewController: HomeAppViewController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_themoviedb)

        movie = findViewById(R.id.movie)
        tv = findViewById(R.id.tv)
        messengerNotFavorites = findViewById(R.id.nenhum_favorito)
        favoritesRecyclerView = findViewById(R.id.favorites)
        loadingFavorite = findViewById(R.id.loading_favorites)

        messengerNotFavorites.becomeInvisible()

        movie.setOnClickListener {
            val startNewActivity = Intent(this, HomeMoviesActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        tv.setOnClickListener {
            val startNewActivity = Intent(this, HomeTvActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        favoritesRecyclerView.setupAdapter(this, MovieDbCategory.MovieFavorite)
    }

    override fun onStart() {
        super.onStart()
        loadingFavorite.becomeVisible()
        loadFavorite()
    }

    private fun loadFavorite() {
        homeAppViewController.getFavorites {
            this.runOnUiThread {
                messengerNotFavorites.becomeVisibleOrInvisible(isToBeVisible = it.isEmpty())
                it.forEach { it.favorite = true }
                loadingFavorite.becomeInvisible()
                favoritesRecyclerView.favoriteAdapter.setMovies(it)
            }
        }
    }

    override fun loadMovies(type: MovieDbCategory) {
        if(type == MovieDbCategory.MovieFavorite) loadFavorite()
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
        homeAppViewController.favoriteMovie(idMovie, toFavorite)
    }

    override fun updateVisualMovie(idMovie: Int, toFavorite: Boolean) {
        val position = favoritesRecyclerView.favoriteAdapter.getMoviePosition(idMovie)
        favoritesRecyclerView.favoriteAdapter.removeMovie(position)
        if(favoritesRecyclerView.favoriteAdapter.hasNoFavorites()) messengerNotFavorites.becomeVisible()
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieDbCategory: MovieDbCategory) {
        this.adapter = RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieDbCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.favoriteAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}