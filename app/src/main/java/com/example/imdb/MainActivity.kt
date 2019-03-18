package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var movies: RecyclerView
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movies = findViewById(R.id.movies)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val list: MutableList<String> = mutableListOf()

        for (i in 0 until 10 step 1)
        {
            val a: String = (Math.random() * 10).toString()
            list.add(i, a)
        }

        recyclerViewAdapter = RecyclerViewAdapter(list)
        movies.adapter = recyclerViewAdapter
        movies.layoutManager = linearLayoutManager
    }
}
