package com.example.imdb.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.imdb.R
import com.example.imdb.ui.homemovies.HomeMoviesActivity
import com.example.imdb.ui.hometv.HomeTvActivity

class HomeAppActivity : AppCompatActivity() {

    private lateinit var movie: Button
    private lateinit var tv: Button
    private lateinit var messengeNotFavorites: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_themoviedb)

        movie = findViewById(R.id.movie)
        tv = findViewById(R.id.tv)
        messengeNotFavorites = findViewById(R.id.nenhum_favorito)

        movie.setOnClickListener {
            val startNewActivity = Intent(this, HomeMoviesActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        tv.setOnClickListener {
            val startNewActivity = Intent(this, HomeTvActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        messengeNotFavorites.visibility = View.VISIBLE
    }
}
