package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.entity.NowPlaying.NowPlaying
import com.example.imdb.entity.NowPlaying.Result
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var movies: RecyclerView
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"

        movies = findViewById(R.id.movies)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val api = API().service()
        val nowPlaying = api.getNowPlaying(apiKey, "en-US", 1)

        nowPlaying.enqueue(object: Callback<NowPlaying?> {
            override fun onFailure(call: Call<NowPlaying?>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<NowPlaying?>, response: Response<NowPlaying?>) {
                response.let {
//                    println(it.body().results)
                    recyclerViewAdapter = RecyclerViewAdapter(it.body()?.results!!.toMutableList())
                    movies.adapter = recyclerViewAdapter
                    movies.layoutManager = linearLayoutManager
                }
            }
        })


    }
}
