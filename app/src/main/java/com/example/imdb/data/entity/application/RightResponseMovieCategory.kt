package com.example.imdb.data.entity.application

import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.data.entity.http.movie.Movie

data class RightResponseMovieCategory(val funResponse: (movies: List<Movie>) -> Unit,
                                      val movies: List<Movie>,
                                      val movieDbCategory: MovieDbCategory,
                                      val favorites: MutableList<Movie>)