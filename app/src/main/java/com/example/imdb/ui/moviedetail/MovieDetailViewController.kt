package com.example.imdb.ui.moviedetail

import android.util.Log
import com.example.imdb.data.DataController
import com.example.imdb.data.entity.http.MovieDetail

class MovieDetailViewController {

    fun loadMovieDetail(id: Int, funResponse: (MovieDetail) -> Unit) {
        Log.i("test", "asdkjskdjaksdjsak2")
        DataController.loadMovieDetail(id, funResponse)
        Log.i("test", "asdkjskdjaksdjsak6")
    }
}