package com.example.imdb.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.ui.homemovies.HomeMoviesActivity
import com.example.imdb.ui.hometv.HomeTvActivity
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList
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

        loadingFavorite.becomeVisible()

        movie.setOnClickListener {
            val startNewActivity = Intent(this, HomeMoviesActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        tv.setOnClickListener {
            val startNewActivity = Intent(this, HomeTvActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        favoritesRecyclerView.setupAdapter(this, MovieCategory.Favorite)

        homeAppViewController.getFavorites {
            this.runOnUiThread {
                if(it.isEmpty())
                    messengerNotFavorites.becomeVisible()

                it.forEach { it.favorite = true }

                loadingFavorite.becomeVisible()
                favoritesRecyclerView.favoriteAdapter.setMovies(it)
            }
        }
    }

    override fun loadMovies(type: MovieCategory) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateVisualMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        val position = favoritesRecyclerView.favoriteAdapter.getMoviePosition(idMovie)
        favoritesRecyclerView.favoriteAdapter.favoriteMovie(position, toFavorite)
    }

    override fun updateVisualMovie(idMovie: Int, toFavorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieCategory: MovieCategory) {
        this.adapter = RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.favoriteAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}