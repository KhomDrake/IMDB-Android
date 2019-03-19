package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.recyclerview.*
import com.example.imdb.entity.Latest
import com.example.imdb.entity.MoviesList
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // recyclers
    private lateinit var nowPlayingRecyclerView: RecyclerView
    private lateinit var latestRecyclerView: RecyclerView
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var topRatedRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView

    // adapters
    private lateinit var recyclerViewAdapterNowPlaying: RecyclerViewAdapterNowPlaying
    private lateinit var recyclerViewAdapterLatest: RecyclerViewAdapterLatest
    private lateinit var recyclerViewAdapterPopular: RecyclerViewAdapterPopular
    private lateinit var recyclerViewAdapterTopRated: RecyclerViewAdapterTopRated
    private lateinit var recyclerViewAdapterUpcoming: RecyclerViewAdapterUpcoming

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"

        nowPlayingRecyclerView = findViewById(R.id.movies)
        latestRecyclerView = findViewById(R.id.latest)
        popularRecyclerView = findViewById(R.id.popular)
        topRatedRecyclerView = findViewById(R.id.toprated)
        upcomingRecyclerView = findViewById(R.id.upcoming)

        val api = API().service()
        val latestRequest = api.getLatest(apiKey, "en-US")
        val nowPlayingRequest = api.getNowPlaying(apiKey, "en-US", 1)
        val popularRequest = api.getPopular(apiKey, "en-US", 1)
        val topRatedRequest = api.getTopRated(apiKey, "en-US", 1)
        val upcomingRequest = api.getUpcoming(apiKey, "en-US", 1)

        popularRequest.enqueue(requestResponse<MoviesList> {
            recyclerViewAdapterPopular = RecyclerViewAdapterPopular(it.results.toMutableList())
            popularRecyclerView.adapter = recyclerViewAdapterPopular
            popularRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(this)
        })

        nowPlayingRequest.enqueue(requestResponse<MoviesList> {
            recyclerViewAdapterNowPlaying = RecyclerViewAdapterNowPlaying(it.results.toMutableList())
            nowPlayingRecyclerView.adapter = recyclerViewAdapterNowPlaying
            nowPlayingRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(this)
        })

        latestRequest.enqueue(requestResponse<Latest> {
            val list = listOf(it)
            recyclerViewAdapterLatest = RecyclerViewAdapterLatest(list)
            latestRecyclerView.adapter = recyclerViewAdapterLatest
            latestRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(this)
        })

        topRatedRequest.enqueue(requestResponse<MoviesList> {
            recyclerViewAdapterTopRated = RecyclerViewAdapterTopRated(it.results.toMutableList())
            topRatedRecyclerView.adapter = recyclerViewAdapterTopRated
            topRatedRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(this)
        })

        upcomingRequest.enqueue(requestResponse<MoviesList> {
            recyclerViewAdapterUpcoming = RecyclerViewAdapterUpcoming(it.results.toMutableList())
            upcomingRecyclerView.adapter = recyclerViewAdapterUpcoming
            upcomingRecyclerView.layoutManager = Auxiliary.getVerticalLinearLayoutManager(this)
        })
    }

    private fun <T>requestResponse(funResponse: (body: T) -> Unit) = object: Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) {

        }

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            funResponse(response.body()!!)
        }
    }
}
