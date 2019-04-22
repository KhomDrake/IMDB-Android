package com.example.imdb.ui.recommendation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.ui.interfaces.IActivityInteraction
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.moviedetail.MovieDetailActivity
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterMovieList
import org.koin.android.ext.android.inject

class RecommendationActivity : AppCompatActivity(), IFavorite {

    private val recommendationViewController: RecommendationViewController by inject()
    private lateinit var recommendationRecyclerView: RecyclerView
    private lateinit var loadingRecommendation: ProgressBar
    private lateinit var recommendationTitle: TextView
    private lateinit var recommendationNoFound: TextView
    private lateinit var title: String
    private var movieID: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        recommendationRecyclerView = findViewById(R.id.recommendation)
        recommendationTitle = findViewById(R.id.recommendation_title)
        recommendationNoFound = findViewById(R.id.no_recommendation)
        loadingRecommendation = findViewById(R.id.loading_recommendation)

        recommendationNoFound.becomeInvisible()

        title = intent.getStringExtra("title")
        movieID = intent.getIntExtra("movieID", -3000)
        if(movieID < 0)
            return

        recommendationTitle.text = "Recommendation: $title"

        loadTryAgain(MovieCategory.Recommendation)
    }

    override fun loadTryAgain(type: MovieCategory) {
        when(type) {
            MovieCategory.Recommendation -> {
                loadingRecommendation.becomeVisible()

                recommendationRecyclerView.setupAdapter(this, MovieCategory.Recommendation)
                recommendationViewController.loadRecommendation(movieID) {
                    if (it.isEmpty()) recommendationNoFound.becomeVisible()

                    this.runOnUiThread {
                        recommendationRecyclerView.movieAdapter.setMovies(it)
                        loadingRecommendation.becomeInvisible()
                    }
                }
            }
            else -> Log.i(TAG_VINI, "Isso não deveria acontecer")
        }
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) {
        val startNewActivity = Intent(view.context, MovieDetailActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            view,
            view.transitionName)
        startNewActivity.putExtra("movieID", movieId)
        startNewActivity.putExtra("url", url)
        ContextCompat.startActivity(view.context, startNewActivity, optionsCompat.toBundle())
    }

    override fun updateVisualMovies() { loadTryAgain(MovieCategory.Recommendation) }

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        recommendationViewController.favoriteMovie(idMovie, toFavorite)
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, movieCategory: MovieCategory) {
        this.adapter = RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, movieCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
