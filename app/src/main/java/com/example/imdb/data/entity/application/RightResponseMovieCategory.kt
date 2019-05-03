package com.example.imdb.data.entity.application

import com.example.imdb.MovieCategory
import com.example.imdb.data.entity.http.Movie

data class RightResponseMovieCategory(val funResponse: (movies: List<Movie>) -> Unit,
                                      val movies: List<Movie>,
                                      val movieCategory: MovieCategory,
                                      val favorites: MutableList<Movie>)