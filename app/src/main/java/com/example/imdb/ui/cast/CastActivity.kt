package com.example.imdb.ui.cast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.MovieCategory
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.ActivityInteraction
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterCast

class CastActivity : AppCompatActivity(), ActivityInteraction {

    private lateinit var castViewController: CastViewController
    private lateinit var recyclerViewCast: RecyclerView
    private lateinit var castTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast)
        castViewController = CastViewController()
        recyclerViewCast = findViewById(R.id.movie_cast)
        castTitle = findViewById(R.id.title_reviews)

        val movieID: Int = intent.getIntExtra("movieID", -1)
        val title: String = intent.getStringExtra("title")

        if(movieID < 0)
            return

        Log.i(TAG_VINI, movieID.toString())

        castTitle.text = "Cast: $title"

        recyclerViewCast.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewCast.adapter = RecyclerViewAdapterCast(mutableListOf(), movieID, this)
        castViewController.loadCast(movieID) {
            recyclerViewCast.castAdapter.setMovieCredit(it.cast)
        }
    }

    override fun loadTryAgain(type: MovieCategory) {

    }

    override fun makeTransition(view: View, movieId: Int, url: String) = Unit

    override fun updateVisualMovies() = Unit

    private val RecyclerView.castAdapter: RecyclerViewAdapterCast
        get() = adapter as RecyclerViewAdapterCast
}
