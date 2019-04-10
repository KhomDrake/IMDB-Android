package com.example.imdb.ui.old.mainactivity

import android.view.View
import com.example.imdb.MovieCategory

interface RequestCategory {

    fun loadCategory(type: MovieCategory)

    fun makeTransition(view: View, movieId: Int, url: String)

}