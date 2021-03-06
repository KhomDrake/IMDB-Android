package com.example.imdb.ui.movies.moviereview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.ui.becomeVisibleOrInvisible
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterReviews
import org.koin.android.ext.android.inject

class ReviewActivity : AppCompatActivity() {

    private val reviewViewController: ReviewViewController by inject()
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

        noReviews.becomeInvisible()

        reviewTitle.text = "Review: $title"

        reviewRecyclerView = findViewById(R.id.reviews)
        loadingReview = findViewById(R.id.loading_review)

        reviewRecyclerView.adapter = RecyclerViewAdapterReviews(mutableListOf())
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        loadReviews()

        tryAgain.setOnClickListener { loadReviews() }
    }

    private fun loadReviews() {
        loadingReview.becomeVisible()
        tryAgain.becomeInvisible()

        reviewViewController.loadReviews(movieID) {
            this.runOnUiThread {
                loadingReview.becomeInvisible()

                if (it.reviews.isNullOrEmpty().not() && it.reviews[0].error) {
                    tryAgain.becomeVisible()
                } else {
                    noReviews.becomeVisibleOrInvisible(isToBeVisible = it.reviews.isNullOrEmpty())
                    reviewRecyclerView.reviewAdapter.setReviews(it)
                }
            }
        }
    }

    private val RecyclerView.reviewAdapter: RecyclerViewAdapterReviews
        get() = adapter as RecyclerViewAdapterReviews
}
