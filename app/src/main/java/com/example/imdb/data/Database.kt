package com.example.imdb.data

import com.example.imdb.entity.Result

object Database {

    private val latest: MutableList<Result> = mutableListOf()
    private val nowPlaying: MutableList<Result> = mutableListOf()
    private val popular: MutableList<Result> = mutableListOf()
    private val topRated: MutableList<Result> = mutableListOf()
    private val upcoming: MutableList<Result> = mutableListOf()

    fun getLatest() = latest

    fun getNowPlaying() = nowPlaying

    fun getPopular() = popular

    fun getTopRated() = topRated

    fun getUpcoming() = upcoming

    fun addLoadingAll(loading: Result) {
        latest.add(0, loading)
        nowPlaying.add(0, loading)
        popular.add(0, loading)
        topRated.add(0, loading)
        upcoming.add(0, loading)
    }

    fun addLatest(movie: Result) {
        removeAt(0, latest)
        latest.add(0, movie)
    }

    fun addNowPlaying(movies: List<Result>) {
        removeAt(0, nowPlaying)
        for (movie in movies) {
            nowPlaying.add(nowPlaying.count(), movie)
        }
    }

    fun addPopular(movies: List<Result>) {
        removeAt(0, popular)
        for (movie in movies) {
            popular.add(popular.count(), movie)
        }
    }

    fun addTopRated(movies: List<Result>) {
        removeAt(0, topRated)
        for (movie in movies) {
            topRated.add(topRated.count(), movie)
        }
    }

    fun addUpcoming(movies: List<Result>) {
        removeAt(0, upcoming)
        for (movie in movies) {
            upcoming.add(upcoming.count(), movie)
        }
    }

    private fun removeAt(i: Int, list: MutableList<Result>) {
        if (list.isEmpty())
            return

        list.removeAt(i)
    }
}