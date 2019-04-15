package com.example.imdb.ui.recommendation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.ui.ActivityInteraction
import com.example.imdb.ui.moviedetail.MovieDetailActivity
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

class RecommendationActivity : AppCompatActivity(), ActivityInteraction {

    private lateinit var recommendationViewController: RecommendationViewController
    private lateinit var recommendationRecyclerView: RecyclerView
    private lateinit var loadingRecommendation: ProgressBar
    private lateinit var recommendationTitle: TextView
    private lateinit var recommendationNoFound: TextView
    private lateinit var title: String
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        recommendationViewController = RecommendationViewController()
        recommendationRecyclerView = findViewById(R.id.recommendation)
        recommendationTitle = findViewById(R.id.recommendation_title)
        recommendationNoFound = findViewById(R.id.no_recommendation)
        loadingRecommendation = findViewById(R.id.loading_recommendation)

        recommendationNoFound.visibility = View.INVISIBLE
        loadingRecommendation.visibility = View.VISIBLE

        title = intent.getStringExtra("title")
        movieID = intent.getIntExtra("movieID", -3000)
        if(movieID < 0)
            return

        recommendationTitle.text = "Recommendation: $title"

        recommendationRecyclerView.setupAdapter(this, MovieCategory.Recommendation)

        loadTryAgain(MovieCategory.Recommendation)
    }

    override fun loadTryAgain(type: MovieCategory) {
        when(type) {
            MovieCategory.Recommendation -> {
                recommendationViewController.loadRecommendation(movieID) {
                    if(it.isEmpty()) recommendationNoFound.visibility = View.VISIBLE
                    recommendationRecyclerView.movieAdapter.setMovies(it)
                    loadingRecommendation.visibility = View.INVISIBLE
                }
            }
            else -> println("eita")
        }
    }

    override fun makeTransition(view: View, movieId: Int, url: String) {
        val startNewActivity = Intent(view.context, MovieDetailActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            view,
            view.transitionName)
        startNewActivity.putExtra("movieID", movieId)
        startNewActivity.putExtra("url", url)
        ContextCompat.startActivity(view.context, startNewActivity, optionsCompat.toBundle())
    }

    override fun updateVisualMovies() {
        loadTryAgain(MovieCategory.Recommendation)
    }

    private fun RecyclerView.setupAdapter(activityInteraction: ActivityInteraction, movieCategory: MovieCategory) {
        this.adapter =  RecyclerViewAdapterMovieList(mutableListOf(), activityInteraction, movieCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
