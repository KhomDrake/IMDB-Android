package com.example.imdb.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.ui.interfaces.IActivityInteraction
import com.example.imdb.ui.homemovies.HomeMoviesActivity
import com.example.imdb.ui.hometv.HomeTvActivity
import org.koin.android.ext.android.inject

class HomeAppActivity : AppCompatActivity(), IActivityInteraction {

    private lateinit var movie: Button
    private lateinit var tv: Button
    private lateinit var messengerNotFavorites: TextView
    private val homeAppViewController: HomeAppViewController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_themoviedb)

        movie = findViewById(R.id.movie)
        tv = findViewById(R.id.tv)
        messengerNotFavorites = findViewById(R.id.nenhum_favorito)

        movie.setOnClickListener {
            val startNewActivity = Intent(this, HomeMoviesActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        tv.setOnClickListener {
            val startNewActivity = Intent(this, HomeTvActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        val favorites = homeAppViewController.getFavorites()

        Log.i(TAG_VINI, favorites.toString())

        messengerNotFavorites.becomeVisible()
    }

    override fun loadTryAgain(type: MovieCategory) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateVisualMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}