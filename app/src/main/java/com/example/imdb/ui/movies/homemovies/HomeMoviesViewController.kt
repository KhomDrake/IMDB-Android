package com.example.imdb.ui.movies.homemovies

import com.example.imdb.data.Repository
import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesViewController(private val repository: Repository) {
    private val loading: Movie =
        Movie(
            0,
            "",
            "",
            "",
            loading = true,
            error = false,
            adult = false,
            favorite = false
        )

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   movieDbCategory: MovieDbCategory,
                   funResponse: (movies: List<Movie>) -> Unit) {
        adapterMovieList.setMovies(mutableListOf(loading))
        repository.loadMovieCategory(movieDbCategory, funResponse)
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        repository.favoriteMovie(idMovie, toFavorite)
    }
}