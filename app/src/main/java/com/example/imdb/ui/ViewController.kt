package com.example.imdb.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.network.APIComunication
import java.util.Locale

object ViewController {

    private var first = true

    fun setupView(recyclers: List<RecyclerView>)
    {
        if(!first)
            return

        val list: List<com.example.imdb.entity.Result> = listOf()
        for (recycler in recyclers)
        {
            recycler.setupAdapter(RecyclerViewAdapterMovieList(list))
        }

        APIComunication.setupDatabase(Locale.getDefault().toLanguageTag())


    }

    private fun RecyclerView.setupAdapter(adapter: RecyclerViewAdapterMovieList) {
        this.adapter = adapter
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

}