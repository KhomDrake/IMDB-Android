package com.example.imdb.ui.moviereview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.ui.ActivityInteraction
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterReviews

class ReviewActivity : AppCompatActivity(), ActivityInteraction {

    private lateinit var reviewViewController: ReviewViewController
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var loadingReview: ProgressBar
    private lateinit var tryAgain: Button
    private lateinit var reviewTitle: TextView
    private lateinit var noReviews: TextView
    private lateinit var title: String
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        tryAgain = findViewById(R.id.again)
        reviewTitle = findViewById(R.id.title_reviews)
        noReviews = findViewById(R.id.no_review)
        title = intent.getStringExtra("title")
        movieID = intent.getIntExtra("movieID", -1)
        if(movieID < 0)
            return

        noReviews.visibility = View.INVISIBLE

        reviewTitle.text = "Review: $title"

        tryAgain.visibility = View.INVISIBLE
        reviewViewController = ReviewViewController()
        reviewRecyclerView = findViewById(R.id.reviews)
        loadingReview = findViewById(R.id.loading_review)
        loadingReview.visibility = View.VISIBLE

        reviewRecyclerView.adapter = RecyclerViewAdapterReviews(mutableListOf(), this)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        loadReviews()
        tryAgain.setOnClickListener {
            loadReviews()
        }
    }

    override fun loadTryAgain(type: MovieCategory) {}

    override fun makeTransition(view: View, movieId: Int, url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateVisualMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadReviews() {
        reviewViewController.loadReviews(movieID) {
            loadingReview.visibility = View.INVISIBLE
            if(it.results.isNotEmpty() && it.results[0].error) {
                tryAgain.visibility = View.VISIBLE
                return@loadReviews
            }

            if(it.results.isEmpty())
                noReviews.visibility = View.VISIBLE

            tryAgain.visibility = View.INVISIBLE
            reviewRecyclerView.reviewAdapter.setReviews(it)
        }
    }

    private val RecyclerView.reviewAdapter: RecyclerViewAdapterReviews
        get() = adapter as RecyclerViewAdapterReviews
}
