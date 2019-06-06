package com.example.imdb.ui.movies.ratemovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.imdb.R
import android.text.Editable
import com.bumptech.glide.Glide
import com.example.imdb.data.entity.http.Rate
import org.koin.android.ext.android.inject

class RateMovieActivity : AppCompatActivity() {

    private lateinit var imagePost: ImageView
    private lateinit var rateEditText: EditText
    private lateinit var rateButton: Button
    private val rateMovieViewController: RateMovieViewController by inject()
    private var idMovie: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_movie)

        imagePost = findViewById(R.id.movie_post_img)
        rateEditText = findViewById(R.id.rate)
        rateButton = findViewById(R.id.rate_button)

        val url = intent.getStringExtra("url")

        idMovie = intent.getIntExtra("movieID", -1)
        Glide.with(this).load(url).into(imagePost)

        rateEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    val rateValue = s.toString().toInt()
                    if (rateValue > 10)
                        rateEditText.setText("10")
                    else if (rateValue < 0)
                        rateEditText.setText("0")
                } catch (e: Exception) {}
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })

        rateButton.setOnClickListener {
            if(rateEditText.text.isBlank() && rateEditText.text.isEmpty()) {
                showInvalidRate()
                return@setOnClickListener
            }

            val rate = rateEditText.text.toString().toInt()

            if(rate < 0 || rate > 10) {
                showInvalidRate()
                return@setOnClickListener
            }

            rateMovieViewController.rateMovie(idMovie, Rate(rate.toDouble())) {
                this.runOnUiThread {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showInvalidRate() {
        Toast.makeText(this, "Avaliação inválida", Toast.LENGTH_SHORT).show()
    }
}
