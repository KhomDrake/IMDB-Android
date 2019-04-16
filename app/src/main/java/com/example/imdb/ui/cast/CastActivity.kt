package com.example.imdb.ui.cast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.ui.ActivityInteraction
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterCast

class CastActivity : AppCompatActivity(), ActivityInteraction {

    private lateinit var castViewController: CastViewController
    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castTitle: TextView
    private lateinit var loading: ProgressBar
    private var movieId: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast)

        castViewController = CastViewController()

        recyclerViewCast = findViewById(R.id.movie_cast)
        castTitle = findViewById(R.id.title_reviews)
        loading = findViewById(R.id.loading)

        movieId = intent.getIntExtra("movieID", -1)
        val title: String = intent.getStringExtra("title")

        if(movieId < 0)
            return

        loading.becomeVisible()

        castTitle.text = "Cast: $title"

        recyclerViewCast.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewCast.adapter = RecyclerViewAdapterCast(mutableListOf(), movieId, this)

        loadTryAgain(MovieCategory.None)
    }

    override fun loadTryAgain(type: MovieCategory) {
        castViewController.loadCast(movieId) {
            loading.becomeInvisible()
            recyclerViewCast.castAdapter.setMovieCredit(it.cast)
        }
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) = Unit

    override fun updateVisualMovies() = Unit

    private val RecyclerView.castAdapter: RecyclerViewAdapterCast
        get() = adapter as RecyclerViewAdapterCast
}
