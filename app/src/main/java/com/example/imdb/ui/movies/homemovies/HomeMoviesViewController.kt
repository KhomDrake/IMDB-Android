package com.example.imdb.ui.movies.homemovies

import com.example.imdb.ui.MovieDbCategory
import com.example.imdb.data.IDataController
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesViewController(private val dataController: IDataController) {
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
                   dbCategory: MovieDbCategory,
                   funResponse: (movies: List<Movie>) -> Unit) {
        adapterMovieList.setMovies(mutableListOf(loading))
        when (dbCategory) {
            MovieDbCategory.MovieLatest -> dataController.loadLatest(funResponse)
            MovieDbCategory.MovieNowPlaying -> dataController.loadNowPlaying(funResponse)
            MovieDbCategory.MoviePopular -> dataController.loadPopular(funResponse)
            MovieDbCategory.MovieTopRated -> dataController.loadTopRated(funResponse)
            MovieDbCategory.MovieUpcoming -> dataController.loadUpcoming(funResponse)
            else -> Unit
        }
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        dataController.favoriteMovie(idMovie, toFavorite)
    }
}