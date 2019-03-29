package com.example.imdb.ui.mainactivity

import com.example.imdb.MovieCategory

interface RequestCategory {

    fun loadCategory(type: MovieCategory)

}