package com.example.imdb.ui.movies.moviedetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.ui.movies.cast.CastActivity
import com.example.imdb.ui.movies.moviereview.ReviewActivity
import com.example.imdb.ui.movies.ratemovie.RateMovieActivity
import com.example.imdb.ui.movies.recommendation.RecommendationActivity
import org.koin.android.ext.android.inject

class MovieDetailActivity : AppCompatActivity() {

    private val movieDetailViewController: MovieDetailViewController by inject()
    private lateinit var progressBar: ProgressBar
    private lateinit var img: ImageView
    private lateinit var title: TextView
    private lateinit var runtime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var voteCount: TextView
    private lateinit var overView: TextView
    private lateinit var recommendation: Button
    private lateinit var review: Button
    private lateinit var rate: Button
    private lateinit var cast: Button
    private lateinit var tryAgain: Button
    private lateinit var overViewText: TextView
    private lateinit var titleText: String
    private lateinit var ratingStars: RatingBar
    private var movieID: Int = -3000

    private val viewsDetailMovie: List<View>
        get() = listOf(title, runtime, releaseDate, voteCount, overView, overViewText, recommendation, review, cast, ratingStars)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        progressBar = findViewById(R.id.loading)
        tryAgain = findViewById(R.id.again)
        img = findViewById(R.id.posterImage)
        title = findViewById(R.id.movie_title)
        runtime = findViewById(R.id.runtime)
        releaseDate = findViewById(R.id.releasedate)
        overView = findViewById(R.id.overview)
        voteCount = findViewById(R.id.vote_count)
        recommendation = findViewById(R.id.recommendations)
        ratingStars = findViewById(R.id.rateStars)
        review = findViewById(R.id.reviews)
        overViewText = findViewById(R.id.overview_text)
        cast = findViewById(R.id.cast)

        ratingStars.isEnabled = false

        tryAgain.becomeInvisible()
        viewsDetailMovie.forEach { it.becomeInvisible() }

        img.becomeVisible()
        progressBar.becomeVisible()

        movieID = intent.getIntExtra("movieID", -1)
        if(movieID < 0)
            return

        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).into(img)

        loadMovieDetail(movieID)

        recommendation.setOnClickListener { nextActivity(Intent(this, RecommendationActivity::class.java)) }
        review.setOnClickListener { nextActivity(Intent(this, ReviewActivity::class.java)) }
        cast.setOnClickListener { nextActivity(Intent(this, CastActivity::class.java)) }
        rate.setOnClickListener { nextActivity(Intent(this, RateMovieActivity::class.java)) }

        tryAgain.setOnClickListener { loadMovieDetail(movieID) }
    }

    private fun nextActivity(startNewActivity: Intent) {
        startNewActivity.setupNextActivity(movieID, titleText)
        startNewActivity.startNextActivity(this)
    }

    private fun Intent.setupNextActivity(_movieID: Int, _titleText: String) {
        this.putExtra("movieID", _movieID)
        this.putExtra("title", _titleText)
    }

    private fun Intent.startNextActivity(ctx: Context) {
        ContextCompat.startActivity(ctx, this, null)
    }

    private fun loadMovieDetail(movieID: Int) {
        progressBar.becomeVisible()
        tryAgain.becomeInvisible()

        movieDetailViewController.loadMovieDetail(movieID) {
            this.runOnUiThread {
                if (it.error) {
                    img.becomeInvisible()
                    progressBar.becomeInvisible()
                    tryAgain.becomeVisible()
                } else {
                    tryAgain.becomeInvisible()
                    img.becomeVisible()

                    viewsDetailMovie.forEach { it.becomeVisible() }

                    val quantStars: Int = movieDetailViewController.getQuantityStars(it.voteAverage)
                    ratingStars.rating = quantStars.toFloat()

                    titleText = it.title

                    val date: String = movieDetailViewController.getDate(it.releaseDate)
                    val runtimeText: String = movieDetailViewController.getRuntime(it.runtime)
                    val overView: String = movieDetailViewController.getOverview(it.overview)
                    val voteCountText: String = movieDetailViewController.getVoteCount(it.voteCount)

                    title.text = movieDetailViewController.getMovieTitle(titleText)
                    runtime.text = runtimeText
                    releaseDate.text = date
                    overViewText.text = overView
                    voteCount.text = voteCountText

                    progressBar.becomeInvisible()
                }
            }
        }
    }
}
