package com.example.imdb.ui.movies.moviedetail

import com.example.imdb.data.entity.http.movie.MovieDetail

class MovieDetailViewController(private val dataController: IDataController) {

    fun loadMovieDetail(id: Int, funResponse: (MovieDetail) -> Unit) {
        dataController.loadMovieDetail(id, funResponse)
    }

    fun getDate(releaseDate: String) : String {
        val releaseDateSplit = releaseDate.split("-")
        if(releaseDateSplit.count() < 3)
            return "Release Date: Unknown"

        val month = releaseDateSplit[1]
        val day = releaseDateSplit[2]
        val year = releaseDateSplit[0]
        return "Release Date: $day/$month/$year"
    }

    fun getRuntime(runtime: Int) = if(runtime <= 0) "Runtime: Unknown" else "Runtime: $runtime min"

    fun getOverview(overview: String) = if(overview.isBlank() || overview.isEmpty()) "Unknown" else overview

    fun getVoteCount(voteCount: Int) = if(voteCount == 0) "Vote Count: Unknown" else "Vote Count: $voteCount"

    fun getQuantStars(voteAverage: Double) = Math.round(Math.round(voteAverage)/2.0).toInt()

    fun getMovieTitle(title: String) = if(title.isBlank() || title.isEmpty()) "Title: Unknown" else
        "Title: $title"
}