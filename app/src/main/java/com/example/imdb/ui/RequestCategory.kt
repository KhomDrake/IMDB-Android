package com.example.imdb.ui

import com.example.imdb.MovieCategory

interface RequestCategory {

    fun loadCategory(type: MovieCategory)

}