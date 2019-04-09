package com.example.imdb.ui.moviereview

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterReviews

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewViewController: ReviewViewController
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var loadingReview: ProgressBar
    private lateinit var tryAgain: Button
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        tryAgain = findViewById(R.id.again)
        movieID = intent.getIntExtra("movieID", -1)
        if(movieID < 0)
            return

        tryAgain.visibility = View.INVISIBLE
        reviewViewController = ReviewViewController()
        reviewRecyclerView = findViewById(R.id.reviews)
        loadingReview = findViewById(R.id.loading_review)
        loadingReview.visibility = View.VISIBLE
        reviewRecyclerView.setupAdapterReviews()

        loadReviews()
        tryAgain.setOnClickListener {
            loadReviews()
        }
    }

    private fun loadReviews() {
        reviewViewController.loadReviews(movieID) {
            loadingReview.visibility = View.INVISIBLE
            if(it.error) {
                tryAgain.visibility = View.VISIBLE
                return@loadReviews
            }
            tryAgain.visibility = View.INVISIBLE
            reviewRecyclerView.reviewAdapter.setReviews(it)
        }
    }

    private fun RecyclerView.setupAdapterReviews() {
        this.adapter = RecyclerViewAdapterReviews(mutableListOf())
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.reviewAdapter: RecyclerViewAdapterReviews
        get() = adapter as RecyclerViewAdapterReviews
}
