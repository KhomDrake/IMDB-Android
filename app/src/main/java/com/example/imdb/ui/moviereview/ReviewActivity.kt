package com.example.imdb.ui.moviereview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterReviews

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewViewController: ReviewViewController
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var loadingReview: ProgressBar
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        movieID = intent.getIntExtra("movieID", -3000)
        if(movieID < 0)
            return

        reviewViewController = ReviewViewController()
        reviewRecyclerView = findViewById(R.id.reviews)
        loadingReview = findViewById(R.id.loading_review)

        loadingReview.visibility = View.VISIBLE
        reviewRecyclerView.setupAdapterReviews()

        println(movieID)

        reviewViewController.loadReviews(movieID) {
            reviewRecyclerView.reviewAdapter.setReviews(it)
            loadingReview.visibility = View.INVISIBLE
        }
    }

    private fun RecyclerView.setupAdapterReviews() {
        this.adapter = RecyclerViewAdapterReviews(mutableListOf())
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.reviewAdapter: RecyclerViewAdapterReviews
        get() = adapter as RecyclerViewAdapterReviews
}
