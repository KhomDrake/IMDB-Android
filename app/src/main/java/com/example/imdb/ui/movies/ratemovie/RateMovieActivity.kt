package com.example.imdb.ui.movies.ratemovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.imdb.R
import com.example.imdb.TAG_VINI

class RateMovieActivity : AppCompatActivity() {

    private lateinit var imagePost: ImageView
    private lateinit var rateEditText: EditText
    private lateinit var rateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_movie)

        imagePost = findViewById(R.id.movie_post_img)
        rateEditText = findViewById(R.id.rate)
        rateButton = findViewById(R.id.rate_button)

        rateButton.setOnClickListener {
            val rate = rateEditText.text.toString().toInt()

            if(rate < 0 || rate > 10) {
                Toast.makeText(this, "Avaliação inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.i(TAG_VINI, "Avaliação feita com sucesso.")
        }
    }
}
