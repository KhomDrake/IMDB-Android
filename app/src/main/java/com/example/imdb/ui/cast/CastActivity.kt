package com.example.imdb.ui.cast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.ui.recyclerview.RecyclerViewAdapterCast

class CastActivity : AppCompatActivity() {

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

        castTitle.text = "Cast: $title"

        recyclerViewCast.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerViewCast.adapter = RecyclerViewAdapterCast(mutableListOf(), movieID)
        castViewController.loadCast(movieID) {
            Log.i(TAG_VINI, it.toString())
            recyclerViewCast.castAdapter.setMovieCredit(it.cast)
        }
    }

    private val RecyclerView.castAdapter: RecyclerViewAdapterCast
        get() = adapter as RecyclerViewAdapterCast
}
