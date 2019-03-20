package com.example.imdb.data

import com.example.imdb.entity.Movie

object Database {

    private val latest: MutableList<Movie> = mutableListOf()
    private val nowPlaying: MutableList<Movie> = mutableListOf()
    private val popular: MutableList<Movie> = mutableListOf()
    private val topRated: MutableList<Movie> = mutableListOf()
    private val upcoming: MutableList<Movie> = mutableListOf()

    fun getLatest() = latest

    fun getNowPlaying() = nowPlaying

    fun getPopular() = popular

    fun getTopRated() = topRated

    fun getUpcoming() = upcoming

    fun addLoadingAll(loading: Movie) {
        latest.add(0, loading)
        nowPlaying.add(0, loading)
        popular.add(0, loading)
        topRated.add(0, loading)
        upcoming.add(0, loading)
    }


    fun addLatest(movie: Movie) {
        removeLoading(latest)
        latest.add(0, movie)
    }

    fun addNowPlaying(movies: List<Movie>) {
        removeLoading(nowPlaying)
        for (movie in movies) {
            nowPlaying.add(nowPlaying.count(), movie)
        }
    }

    fun addPopular(movies: List<Movie>) {
        removeLoading(popular)
        for (movie in movies) {
            popular.add(popular.count(), movie)
        }
    }

    fun addTopRated(movies: List<Movie>) {
        removeLoading(topRated)
        for (movie in movies) {
            topRated.add(topRated.count(), movie)
        }
    }

    fun addUpcoming(movies: List<Movie>) {
        removeLoading(upcoming)
        for (movie in movies) {
            upcoming.add(upcoming.count(), movie)
        }
    }

    fun addLoadingNowPlaying() {
        nowPlaying.add(nowPlaying.count(), Movie("", "", "", true))
    }

    fun addLoadingTopRated() {
        topRated.add(topRated.count(), Movie("", "", "", true))
    }

    fun addLoadingPopular() {
        popular.add(popular.count(), Movie("", "", "", true))
    }

    fun addLoadingUpcoming() {
        upcoming.add(upcoming.count(), Movie("", "", "", true))
    }

    private fun removeLoading(list: MutableList<Movie>) {
        for (item in list) {
            if (item.loading) {
                list.remove(item)
            }
        }
    }
}