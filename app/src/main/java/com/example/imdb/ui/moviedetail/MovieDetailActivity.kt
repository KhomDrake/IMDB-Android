package com.example.imdb.ui.moviedetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var voteCount: TextView
    private lateinit var overView: TextView
    private lateinit var recommendation: Button
    private lateinit var review: Button
    private lateinit var tryAgain: Button
    private lateinit var cast: TextView
    private lateinit var castRecycler: RecyclerView
    private lateinit var overViewText: TextView
    private val viewsDetailMovie: List<View>
        get() = listOf(title, runtime, releaseDate, voteCount, overView, overViewText, recommendation, review, cast)

    private val listOfStars: List<ImageView>
        get() = listOf(findViewById(R.id.first_star),findViewById(R.id.second_star),findViewById(R.id.third_star),findViewById(R.id.fourth_star),findViewById(R.id.third_star))

    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val imgNotFound = "http://hotspottagger.com/locationimages/noimage.jpg"

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
        castRecycler = findViewById(R.id.cast_recycler)

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

            viewsDetailMovie.forEach {
                it.visibility = View.VISIBLE
            }

            val quantStars = Math.round(Math.round(it.voteAverage)/2.0).toInt()

            for (i in 0 until quantStars) {
                listOfStars[i].visibility = View.VISIBLE
            }

            val releaseDateSplit = it.releaseDate.split("-")
            val month = releaseDateSplit[1]
            val day = releaseDateSplit[2]
            val year = releaseDateSplit[0]
            title.text = "${it.title}"
            runtime.text = "Runtime: ${it.runtime} min"
            releaseDate.text = "Release Date: $day/$month/$year"
            overViewText.text = "${it.overview}"
            voteCount.text = "Vote Count: ${it.voteCount}"

            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun getPath(path: String?, urlImg: String, imgNotFound: String) =
        if (path == "null" || path == "" || path == null) imgNotFound else urlImg + path

}
