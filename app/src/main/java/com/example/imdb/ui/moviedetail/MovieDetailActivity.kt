package com.example.imdb.ui.moviedetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.imdb.R

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewController: MovieDetailViewController
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var img: ImageView
    private lateinit var title: TextView
    private lateinit var runtime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var overView: TextView

    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val imgNotFound = "https://uae.microless.com/cdn/no_image.jpg"


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        constraintLayout = findViewById(R.id.details)
        progressBar = findViewById(R.id.loading)
        movieDetailViewController = MovieDetailViewController()

        img = findViewById(R.id.postImg)
        title = findViewById(R.id.title)
        runtime = findViewById(R.id.runtime)
        releaseDate = findViewById(R.id.releasedate)
        voteAverage = findViewById(R.id.voteaverage)
        overView = findViewById(R.id.overview)
        voteCount = findViewById(R.id.votecount)

        progressBar.visibility = View.VISIBLE
        constraintLayout.visibility = View.INVISIBLE

        val movieID: Int = intent.getIntExtra("movieID", -3000)

        if(movieID < 0)
            return

        movieDetailViewController.loadMovieDetail(movieID) {
            progressBar.visibility = View.INVISIBLE
            constraintLayout.visibility = View.VISIBLE

            val path = getPath(it.posterPath, urlImg, imgNotFound)
            Glide.with(this).load(path).into(img)
            title.text = "Title: ${it.title}"
            runtime.text = "Runtime: ${it.runtime}"
            releaseDate.text = "Release Date: ${it.releaseDate}"
            voteAverage.text = "Vote Average: ${it.voteAverage}"
            overView.text = "Over View: ${it.overview}"
            voteCount.text = "Vote Count: ${it.voteCount}"
        }
    }

    private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
        if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path

}
