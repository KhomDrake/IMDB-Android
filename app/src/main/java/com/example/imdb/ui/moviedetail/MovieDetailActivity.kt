package com.example.imdb.ui.moviedetail

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
import com.example.imdb.ui.cast.CastActivity
import com.example.imdb.ui.moviereview.ReviewActivity
import com.example.imdb.ui.recommendation.RecommendationActivity

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewController: MovieDetailViewController
    private lateinit var progressBar: ProgressBar
    private lateinit var img: ImageView
    private lateinit var title: TextView
    private lateinit var runtime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var voteCount: TextView
    private lateinit var overView: TextView
    private lateinit var recommendation: Button
    private lateinit var review: Button
    private lateinit var cast: Button
    private lateinit var tryAgain: Button
    private lateinit var overViewText: TextView
    private lateinit var titleText: String
    private val viewsDetailMovie: List<View>
        get() = listOf(title, runtime, releaseDate, voteCount, overView, overViewText, recommendation, review, cast)

    private val listOfStars: List<ImageView>
        get() = listOf(findViewById(R.id.first_star),findViewById(R.id.second_star),findViewById(R.id.third_star),findViewById(R.id.fourth_star),findViewById(R.id.fifth_star))

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        progressBar = findViewById(R.id.loading)
        movieDetailViewController = MovieDetailViewController()

        listOfStars.forEach { it.visibility = View.INVISIBLE }
        tryAgain = findViewById(R.id.again)
        img = findViewById(R.id.postImg)
        title = findViewById(R.id.movie_title)
        runtime = findViewById(R.id.runtime)
        releaseDate = findViewById(R.id.releasedate)
        overView = findViewById(R.id.overview)
        voteCount = findViewById(R.id.votecount)
        recommendation = findViewById(R.id.recommendations)
        review = findViewById(R.id.reviews)
        overViewText = findViewById(R.id.overview_text)
        cast = findViewById(R.id.cast)

        tryAgain.visibility = View.INVISIBLE
        img.visibility = View.VISIBLE
        viewsDetailMovie.forEach { it.visibility = View.INVISIBLE }
        progressBar.visibility = View.VISIBLE
        val movieID: Int = intent.getIntExtra("movieID", -1)
        if(movieID < 0)
            return

        val url = intent.getStringExtra("url")
        loadMovieDetail(url, movieID)
        recommendation.setOnClickListener {
            val startNewActivity = Intent(this, RecommendationActivity::class.java)
            startNewActivity.putExtra("movieID", movieID)
            startNewActivity.putExtra("title", titleText)
            ContextCompat.startActivity(this, startNewActivity, null)
        }
        review.setOnClickListener {
            val startNewActivity = Intent(this, ReviewActivity::class.java)
            startNewActivity.putExtra("movieID", movieID)
            startNewActivity.putExtra("title", titleText)
            ContextCompat.startActivity(this, startNewActivity, null)
        }
        cast.setOnClickListener {
            val startNewActivity = Intent(this, CastActivity::class.java)
            startNewActivity.putExtra("movieID", movieID)
            startNewActivity.putExtra("title", titleText)
            ContextCompat.startActivity(this, startNewActivity, null)
        }
        tryAgain.setOnClickListener {
            loadMovieDetail(url, movieID)
        }
    }

    private fun loadMovieDetail(url: String, movieID: Int) {
        progressBar.visibility = View.VISIBLE
        tryAgain.visibility = View.INVISIBLE
        Glide.with(this).load(url).into(img)
        movieDetailViewController.loadMovieDetail(movieID) {
            if (it.error) {
                img.visibility = View.INVISIBLE
                tryAgain.visibility = View.VISIBLE
                return@loadMovieDetail
            }
            tryAgain.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE

            viewsDetailMovie.forEach { it.visibility = View.VISIBLE }

            val quantStars: Int = movieDetailViewController.getQuantStars(it.voteAverage)

            for (i in 0 until quantStars) { listOfStars[i].visibility = View.VISIBLE }

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

            progressBar.visibility = View.INVISIBLE
        }
    }
}
