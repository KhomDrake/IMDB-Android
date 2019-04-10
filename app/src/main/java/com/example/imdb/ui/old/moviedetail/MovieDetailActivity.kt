package com.example.imdb.ui.old.moviedetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.ui.old.moviereview.ReviewActivity
import com.example.imdb.ui.old.recommendation.RecommendationActivity

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewController: MovieDetailViewController
    private lateinit var progressBar: ProgressBar
    private lateinit var img: ImageView
    private lateinit var title: TextView
    private lateinit var runtime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var overView: TextView
    private lateinit var recommendation: Button
    private lateinit var review: Button
    private lateinit var tryAgain: Button
    private val viewsDetailMovie: List<View>
        get() = listOf(title, runtime, releaseDate, voteAverage, voteCount, overView, recommendation, review)

    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val imgNotFound = "https://uae.microless.com/cdn/no_image.jpg"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        progressBar = findViewById(R.id.loading)
        movieDetailViewController = MovieDetailViewController()

        tryAgain = findViewById(R.id.again)
        img = findViewById(R.id.postImg)
        title = findViewById(R.id.title)
        runtime = findViewById(R.id.runtime)
        releaseDate = findViewById(R.id.releasedate)
        voteAverage = findViewById(R.id.voteaverage)
        overView = findViewById(R.id.overview)
        voteCount = findViewById(R.id.votecount)
        recommendation = findViewById(R.id.recommendations)
        review = findViewById(R.id.reviews)


        tryAgain.visibility = View.INVISIBLE
        img.visibility = View.VISIBLE

        viewsDetailMovie.forEach {
            it.visibility = View.INVISIBLE
        }
        progressBar.visibility = View.VISIBLE
        val movieID: Int = intent.getIntExtra("movieID", -1)

        if(movieID < 0)
            return

        val url = intent.getStringExtra("url")

        loadMovieDetail(url, movieID)

        recommendation.setOnClickListener {
            val startNewActivity = Intent(this, RecommendationActivity::class.java)
            startNewActivity.putExtra("movieID", movieID)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        review.setOnClickListener {
            val startNewActivity = Intent(this, ReviewActivity::class.java)
            startNewActivity.putExtra("movieID", movieID)
            ContextCompat.startActivity(this, startNewActivity, null)
        }

        tryAgain.setOnClickListener {
            loadMovieDetail(url, movieID)
        }

    }

    private fun loadMovieDetail(url: String, movieID: Int) {
        Glide.with(this).load(url).into(img)
        movieDetailViewController.loadMovieDetail(movieID) {
            progressBar.visibility = View.INVISIBLE

            if (it.error) {
                img.visibility = View.INVISIBLE
                tryAgain.visibility = View.VISIBLE
                return@loadMovieDetail
            }
            tryAgain.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE

            viewsDetailMovie.forEach {
                it.visibility = View.VISIBLE
            }

            val path = getPath(it.posterPath, urlImg, imgNotFound)
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
