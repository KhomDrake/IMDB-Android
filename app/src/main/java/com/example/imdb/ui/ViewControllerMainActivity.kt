package com.example.imdb.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.data.DataController
import com.example.imdb.entity.MoviesList
import com.example.imdb.entity.Movie
import java.util.Locale

object ViewControllerMainActivity {

    private var first = true
    private lateinit var nowPlaying: RecyclerView
    private lateinit var latest: RecyclerView
    private lateinit var popular: RecyclerView
    private lateinit var topRated: RecyclerView
    private lateinit var upcoming: RecyclerView
    private lateinit var linearLayoutManagerNowPlaying: LinearLayoutManager
    private lateinit var linearLayoutManagerPopular: LinearLayoutManager
    private lateinit var linearLayoutManagerTopRated: LinearLayoutManager
    private lateinit var linearLayoutManagerUpcoming: LinearLayoutManager
    private var pagPopular = 1
    private var pagNowPlaying = 1
    private var pagTopRated = 1
    private var pagUpcoming = 1
    private var maxPagPopular = 0
    private var maxPagNowPlaying = 0
    private var maxPagTopRated = 0
    private var maxPagUpcoming = 0

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

        this.latest.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getLatest())
        )

        linearLayoutManagerNowPlaying =
            LinearLayoutManager(this.nowPlaying.context, LinearLayoutManager.HORIZONTAL, false)
        this.nowPlaying.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getNowPlaying()), linearLayoutManagerNowPlaying
        )

        linearLayoutManagerPopular = LinearLayoutManager(this.popular.context, LinearLayoutManager.HORIZONTAL, false)
        this.popular.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getPopular()), linearLayoutManagerPopular
        )

        linearLayoutManagerTopRated = LinearLayoutManager(this.topRated.context, LinearLayoutManager.HORIZONTAL, false)
        this.topRated.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getTopRated()), linearLayoutManagerTopRated
        )

        linearLayoutManagerUpcoming = LinearLayoutManager(this.upcoming.context, LinearLayoutManager.HORIZONTAL, false)
        this.upcoming.setupAdapter(
            RecyclerViewAdapterMovieList(DataController.getUpcoming()), linearLayoutManagerUpcoming
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

    fun listening() {
        scroll(nowPlaying, linearLayoutManagerNowPlaying) {
            pagNowPlaying++

            if (maxPagNowPlaying >= pagNowPlaying) {
                DataController.addLoadingNowPlaying()
                DataController.loadNowPlaying(pagNowPlaying) {
                    DataController.addNowPlaying(it.results)
                    nowPlaying.adapter!!.notifyDataSetChanged()
                }
            }
        }

        scroll(topRated, linearLayoutManagerTopRated) {
            pagTopRated++

            if (maxPagTopRated >= pagTopRated) {
                DataController.addLoadingTopRated()
                DataController.loadTopRated(pagNowPlaying) {
                    DataController.addTopRated(it.results)
                    topRated.adapter!!.notifyDataSetChanged()
                }
            }
        }

        scroll(popular, linearLayoutManagerTopRated) {
            pagPopular++

            if (maxPagPopular >= pagPopular) {
                DataController.addLoadingPopular()
                DataController.loadPopular(pagNowPlaying) {
                    DataController.addPopular(it.results)
                    popular.adapter!!.notifyDataSetChanged()
                }
            }
        }

        scroll(upcoming, linearLayoutManagerPopular) {
            pagUpcoming++

            if (maxPagUpcoming >= pagUpcoming) {
                DataController.addLoadingUpcoming()
                DataController.loadUpcoming(pagNowPlaying) {
                    DataController.addUpcoming(it.results)


                    upcoming.adapter!!.notifyDataSetChanged()
                }
            }
        }

    }

    private fun scroll(recyclerView: RecyclerView, linearLayoutManager: LinearLayoutManager, load: () -> Unit) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (totalItemCount == linearLayoutManager.findLastVisibleItemPosition()) {
                    load()
                }
            }
        })
    }

    private fun loadingRecyclers() {
        DataController.loadLatest {
            DataController.addLatest(it)
            latest.adapter!!.notifyDataSetChanged()
        }

        DataController.loadNowPlaying {
            maxPagNowPlaying = it.totalPages
            DataController.addNowPlaying(it.results)
            nowPlaying.adapter!!.notifyDataSetChanged()
        }

        DataController.loadTopRated {
            maxPagTopRated = it.totalPages
            (topRated.adapter as RecyclerViewAdapterMovieList).addMovie()
            DataController.addTopRated(it.results)
            topRated.adapter!!.notifyDataSetChanged()
        }

        DataController.loadPopular {
            maxPagPopular = it.totalPages
            DataController.addPopular(it.results)
            popular.adapter!!.notifyDataSetChanged()
        }

        DataController.loadUpcoming {
            maxPagUpcoming = it.totalPages
            DataController.addUpcoming(it.results)
            upcoming.adapter!!.notifyDataSetChanged()
        }

        lala(DataController::loadUpcoming, DataController::addUpcoming, upcoming.adapter!!)
        la(DataController::loadUpcoming, upcoming.adapter)
    }

    fun lala(
        algumaCoisa: ((MoviesList) -> Unit) -> Unit,
        data: (List<Movie>) -> Unit,
        adapter: RecyclerView.Adapter<*>
    ) {
        algumaCoisa.invoke {
            data.invoke(it.results)
            adapter.notifyDataSetChanged()
        }
    }

    private fun RecyclerView.setupAdapter(adapter: RecyclerViewAdapterMovieList) {
        this.adapter = adapter
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun RecyclerView.setupAdapter(
        adapter: RecyclerViewAdapterMovieList,
        linearLayoutManager: LinearLayoutManager
    ) {
        this.adapter = adapter
        this.layoutManager = linearLayoutManager
    }
}