package com.example.imdb.ui.cast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.auxiliary.becomeInvisible
import com.example.imdb.auxiliary.becomeVisible
import com.example.imdb.ui.interfaces.IActivityInteraction
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterCast
import org.koin.android.ext.android.inject

class CastActivity : AppCompatActivity(), IActivityInteraction {

    private val castViewController: CastViewController by inject()
    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castTitle: TextView
    private lateinit var loading: ProgressBar
    private lateinit var noCast: TextView
    private var movieId: Int = -3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast)

        recyclerViewCast = findViewById(R.id.movie_cast)
        castTitle = findViewById(R.id.title_reviews)
        loading = findViewById(R.id.loading)
        noCast = findViewById(R.id.no_cast)

        movieId = intent.getIntExtra("movieID", -1)
        val title: String = intent.getStringExtra("title")

        if(movieId < 0)
            return

        loading.becomeVisible()
        noCast.becomeInvisible()

        castTitle.text = "Cast: $title"

        recyclerViewCast.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewCast.adapter = RecyclerViewAdapterCast(mutableListOf(), movieId, this)

        loadMovies(MovieCategory.None)
    }

    override fun loadMovies(type: MovieCategory) {
        castViewController.loadCast(movieId) {
            this.runOnUiThread {
                loading.becomeInvisible()
                recyclerViewCast.castAdapter.setMovieCredit(it.cast)
                if(it.cast.isEmpty())
                    noCast.becomeVisible()
            }
        }
    }

    override fun makeImageTransition(view: View, movieId: Int, url: String) = Unit

    override fun updateVisualMovies() = Unit

    private val RecyclerView.castAdapter: RecyclerViewAdapterCast
        get() = adapter as RecyclerViewAdapterCast
}
