package com.example.imdb.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.data.DataController
import java.util.Locale

object ViewControllerMainActivity {

    private var first = true
    private lateinit var nowPlaying: RecyclerView
    private lateinit var latest: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView


    fun setupView(
        now: RecyclerView,
        latest: RecyclerView,
        popular: RecyclerView,
        top: RecyclerView,
        upcoming: RecyclerView
    ) {
        this.nowPlaying = now
        this.latest = latest
        this.popular = popular
        this.topRated = top
        this.upcoming = upcoming

        this.nowPlaying.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getNowPlaying())
        )
        this.latest.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getLatest())
        )
        this.popular.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getPopular())
        )
        this.topRated.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getTopRated())
        )
        this.upcoming.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getUpcoming())
        )

        if (!first)
            return

        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
        this.latest.adapter!!.notifyDataSetChanged()
        this.nowPlaying.adapter!!.notifyDataSetChanged()
        this.topRated.adapter!!.notifyDataSetChanged()
        this.popular.adapter!!.notifyDataSetChanged()
        this.upcoming.adapter!!.notifyDataSetChanged()
        loadingRecyclers()
        first = false
    }

    private fun loadingRecyclers() {
        DataController.loadLatest {
            DataController.addLatest(it)
            latest.adapter!!.notifyDataSetChanged()
        }

        DataController.loadNowPlaying {
            DataController.addNowPlaying(it.results)
            nowPlaying.adapter!!.notifyDataSetChanged()
        }

        DataController.loadTopRated {
            DataController.addTopRated(it.results)
            topRated.adapter!!.notifyDataSetChanged()
        }

        DataController.loadPopular {
            DataController.addPopular(it.results)
            popular.adapter!!.notifyDataSetChanged()
        }

        DataController.loadUpcoming {
            DataController.addUpcoming(it.results)
            upcoming.adapter!!.notifyDataSetChanged()
        }
    }

    private fun RecyclerView.setupAdapter(adapter: RecyclerViewAdapterMovieList) {
        this.adapter = adapter
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}