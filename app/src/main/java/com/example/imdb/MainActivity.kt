package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.Auxiliary.apiKey
import com.example.imdb.recyclerview.*
import com.example.imdb.entity.Latest
import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Result
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    // recyclers
    private lateinit var nowPlayingRecyclerView: RecyclerView
    private lateinit var latestRecyclerView: RecyclerView
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var topRatedRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nowPlayingRecyclerView = findViewById(R.id.movies)
        latestRecyclerView = findViewById(R.id.latest)
        popularRecyclerView = findViewById(R.id.popular)
        topRatedRecyclerView = findViewById(R.id.toprated)
        upcomingRecyclerView = findViewById(R.id.upcoming)

        fetchData()
    }

    private fun fetchData() = with(API().service()){
        val latestRequest = getLatest(apiKey, "en-US")
        val nowPlayingRequest = getNowPlaying(apiKey, "en-US", 1)
        val popularRequest = getPopular(apiKey, "en-US", 1)
        val topRatedRequest = getTopRated(apiKey, "en-US", 1)
        val upcomingRequest = getUpcoming(apiKey, Locale.getDefault()., 1)

        popularRequest.enqueue(requestResponse<MoviesList> {
            setupRecyclerMovieList(RecyclerViewAdapterMovieList(it.results), popularRecyclerView)
        })

        nowPlayingRequest.enqueue(requestResponse<MoviesList> {
            setupRecyclerMovieList(RecyclerViewAdapterMovieList(it.results), nowPlayingRecyclerView)
        })

        latestRequest.enqueue(requestResponse<Result> {
            val list = listOf(it)
            latestRecyclerView.adapter = RecyclerViewAdapterMovieList(list)
            latestRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(latestRecyclerView.context)
        })

        topRatedRequest.enqueue(requestResponse<MoviesList> {
            setupRecyclerMovieList(RecyclerViewAdapterMovieList(it.results), topRatedRecyclerView)
        })

        upcomingRequest.enqueue(requestResponse<MoviesList> {
            upcomingRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(it.results))
            setupRecyclerMovieList(RecyclerViewAdapterMovieList(it.results), upcomingRecyclerView)
        })
    }

    private fun setupRecyclerMovieList(adapter: RecyclerViewAdapterMovieList, recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun RecyclerView.setupAdapter(adapter: RecyclerViewAdapterMovieList) {
        this.adapter = adapter
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun <T>requestResponse(funResponse: (body: T) -> Unit) = object: Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) = Unit

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            response.body()?.apply(funResponse)
        }
    }
}
