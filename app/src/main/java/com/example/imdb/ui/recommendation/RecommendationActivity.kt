package com.example.imdb.ui.recommendation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.ui.mainactivity.RequestCategory
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList

class RecommendationActivity : AppCompatActivity(), RequestCategory {

    private lateinit var recommendationViewController: RecommendationViewController
    private lateinit var recommendationRecyclerView: RecyclerView
    private lateinit var loadingRecommendation: ProgressBar
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        recommendationViewController = RecommendationViewController()
        recommendationRecyclerView = findViewById(R.id.recommendation)
        loadingRecommendation = findViewById(R.id.loading_recommendation)

        loadingRecommendation.visibility = View.VISIBLE


        movieID = intent.getIntExtra("movieID", -3000)
        if(movieID < 0)
            return

        recommendationRecyclerView.setupAdapter(this, MovieCategory.Recommendation)

        loadCategory(MovieCategory.Recommendation)
    }

    override fun loadCategory(type: MovieCategory) {
        when(type) {
            MovieCategory.Recommendation -> {
                recommendationViewController.loadRecommendation(movieID) {
                    recommendationRecyclerView.movieAdapter.setMovies(it)
                    loadingRecommendation.visibility = View.INVISIBLE
                }
            }
            else -> println("eita")
        }
    }

    private fun RecyclerView.setupAdapter(requestCategory: RequestCategory, movieCategory: MovieCategory) {
        this.adapter =  RecyclerViewAdapterMovieList(mutableListOf(), requestCategory, movieCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}