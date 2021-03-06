package com.example.imdb.ui.movies.recommendation

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
import com.example.imdb.ui.TheMovieDbCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.becomeInvisible
import com.example.imdb.ui.becomeVisible
import com.example.imdb.ui.becomeVisibleOrInvisible
import com.example.imdb.ui.interfaces.IFavorite
import com.example.imdb.ui.movies.moviedetail.MovieDetailActivity
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList
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
    }

    override fun onStart() {
        super.onStart()
        title = intent.getStringExtra("title")
        movieID = intent.getIntExtra("movieID", -3000)
        if(movieID < 0)
            return

        recommendationTitle.text = "MovieRecommendation: $title"
        loadMovies(TheMovieDbCategory.MovieRecommendation)
    }

    override fun loadMovies(type: TheMovieDbCategory) {
        when (type) {
            TheMovieDbCategory.MovieRecommendation -> {
                loadingRecommendation.becomeVisible()

                recommendationRecyclerView.setupAdapter(this, TheMovieDbCategory.MovieRecommendation)
                recommendationViewController.loadRecommendation(movieID) {
                    this.runOnUiThread {
                        recommendationNoFound.becomeVisibleOrInvisible(isToBeVisible = it.isEmpty())

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

    override fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        recommendationViewController.favoriteMovie(idMovie, toFavorite)
    }

    override fun updateVisualMovie(idMovie: Int, toFavorite: Boolean) {
        val position = recommendationRecyclerView.movieAdapter.getMoviePosition(idMovie)
        recommendationRecyclerView.movieAdapter.favoriteMovie(position, toFavorite)
    }

    private fun RecyclerView.setupAdapter(iFavorite: IFavorite, theMovieDbCategory: TheMovieDbCategory) {
        this.adapter = RecyclerViewAdapterMovieList(mutableListOf(), iFavorite, theMovieDbCategory)
        this.layoutManager = LinearLayoutManager(context)
    }

    private val RecyclerView.movieAdapter: RecyclerViewAdapterMovieList
        get() = adapter as RecyclerViewAdapterMovieList
}
