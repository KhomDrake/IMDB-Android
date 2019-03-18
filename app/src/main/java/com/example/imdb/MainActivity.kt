package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.Recyclers.*
import com.example.imdb.entity.Latest.Latest
import com.example.imdb.entity.NowPlaying.NowPlaying
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // recyclers
    private lateinit var nowPlayingRecyclerView: RecyclerView
    private lateinit var latestRecyclerView: RecyclerView

    // adapters
    private lateinit var recyclerViewAdapterNowPlaying: RecyclerViewAdapterNowPlaying
    private lateinit var recyclerViewAdapterLatest: RecyclerViewAdapterLatest
    private lateinit var recyclerViewAdapterPopular: RecyclerViewAdapterPopular
    private lateinit var recyclerViewAdapterTopRated: RecyclerViewAdapterTopRated
    private lateinit var recyclerViewAdapterUpcoming: RecyclerViewAdapterUpcoming

    // linearLayouts
    private lateinit var linearLayoutManagerNowPlaying: LinearLayoutManager
    private lateinit var linearLayoutManagerLatest: LinearLayoutManager
    private lateinit var linearLayoutManagerPopular: LinearLayoutManager
    private lateinit var linearLayoutManagerTopRated: LinearLayoutManager
    private lateinit var linearLayoutManagerUpcoming: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"

        nowPlayingRecyclerView = findViewById(R.id.movies)
        latestRecyclerView = findViewById(R.id.latest)

        linearLayoutManagerNowPlaying = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManagerLatest = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManagerPopular = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManagerTopRated = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManagerUpcoming = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val api = API().service()
        val latestRequest = api.getLatest(apiKey, "en-US")
        val nowPlayingRequest = api.getNowPlaying(apiKey, "en-US", 1)

        latestRequest.enqueue(object: Callback<Latest?> {
            override fun onFailure(call: Call<Latest?>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<Latest?>, response: Response<Latest?>) {
                response.let {
                    val list = listOf(it.body()!!)
                    println(it.body())
                    recyclerViewAdapterLatest = RecyclerViewAdapterLatest(list)
                    latestRecyclerView.adapter = recyclerViewAdapterLatest
                    latestRecyclerView.layoutManager = linearLayoutManagerLatest
                }
            }
        })

        nowPlayingRequest.enqueue(object: Callback<NowPlaying?> {
            override fun onFailure(call: Call<NowPlaying?>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<NowPlaying?>, response: Response<NowPlaying?>) {
                response.let {
                    recyclerViewAdapterNowPlaying = RecyclerViewAdapterNowPlaying(it.body()?.results!!.toMutableList())
                    nowPlayingRecyclerView.adapter = recyclerViewAdapterNowPlaying
                    nowPlayingRecyclerView.layoutManager = linearLayoutManagerNowPlaying
                }
            }
        })


    }
}
