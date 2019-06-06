package com.example.imdb.ui.movies.homemovies

import com.example.imdb.data.Repository
import com.example.imdb.ui.TheMovieDbCategory
import com.example.imdb.data.entity.http.movie.Movie
import com.example.imdb.ui.movies.recyclerview.RecyclerViewAdapterMovieList

class HomeMoviesViewController(private val repository: Repository) {

    private val pagesMovieCategory = hashMapOf<TheMovieDbCategory, Int>()

    init {
        pagesMovieCategory[TheMovieDbCategory.MovieLatest] = 1
        pagesMovieCategory[TheMovieDbCategory.MovieUpcoming] = 1
        pagesMovieCategory[TheMovieDbCategory.MoviePopular] = 1
        pagesMovieCategory[TheMovieDbCategory.MovieNowPlaying] = 1
        pagesMovieCategory[TheMovieDbCategory.MovieTopRated] = 1
    }

    fun getPage(theMovieDbCategory: TheMovieDbCategory) = pagesMovieCategory.get(theMovieDbCategory)!!

    fun addPage(theMovieDbCategory: TheMovieDbCategory) = when(theMovieDbCategory) {
        TheMovieDbCategory.MovieLatest -> Unit
        else -> pagesMovieCategory.set(theMovieDbCategory, pagesMovieCategory[theMovieDbCategory]!! + 1)
    }

    private val loading: Movie =
        Movie(
            id = 0,
            originalTitle = "",
            posterPath = "",
            title =  "",
            loading = true,
            error = false,
            adult = false,
            favorite = false
        )

    fun loadMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                   funResponse: (movies: List<Movie>) -> Unit) {
        adapterMovieList.addMovies(mutableListOf(loading))
        repository.loadMovieCategory(adapterMovieList.theMovieDbCategory, funResponse)
    }

    fun loadNextMovies(adapterMovieList: RecyclerViewAdapterMovieList,
                       funResponse: (movies: List<Movie>) -> Unit) {
        if(adapterMovieList.theMovieDbCategory == TheMovieDbCategory.MovieLatest) return

        adapterMovieList.addMovies(mutableListOf(loading))
        repository.loadMovieCategory(adapterMovieList.theMovieDbCategory, funResponse, getPage(adapterMovieList.theMovieDbCategory))
    }

    fun favoriteMovie(idMovie: Int, toFavorite: Boolean) {
        repository.favoriteMovie(idMovie, toFavorite)
    }
}