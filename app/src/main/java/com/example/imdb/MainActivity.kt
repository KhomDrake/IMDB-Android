package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.ui.*
import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Result
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

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

        ViewController.setupView(listOf(
            nowPlayingRecyclerView,
            latestRecyclerView,
            popularRecyclerView,
            topRatedRecyclerView,
            upcomingRecyclerView))

        fetchData()
    }

    private fun fetchData() = with(API().service()){

        val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"
        val latestRequest = getLatest(apiKey, Locale.getDefault().toLanguageTag())
        val nowPlayingRequest = getNowPlaying(apiKey, Locale.getDefault().toLanguageTag(), 1)
        val popularRequest = getPopular(apiKey, Locale.getDefault().toLanguageTag(), 1)
        val topRatedRequest = getTopRated(apiKey, Locale.getDefault().toLanguageTag(), 1)
        val upcomingRequest = getUpcoming(apiKey, Locale.getDefault().toLanguageTag(), 1)

        popularRequest.enqueue(requestResponse<MoviesList> {
            popularRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(it.results))
        })

        nowPlayingRequest.enqueue(requestResponse<MoviesList> {
            nowPlayingRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(it.results))
        })

        latestRequest.enqueue(requestResponse<Result> {
            val list = listOf(it)
            latestRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(list))
        })

        topRatedRequest.enqueue(requestResponse<MoviesList> {
            topRatedRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(it.results))
        })

        upcomingRequest.enqueue(requestResponse<MoviesList> {
            upcomingRecyclerView.setupAdapter(RecyclerViewAdapterMovieList(it.results))
        })
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
