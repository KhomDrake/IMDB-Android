package com.example.imdb.entity

import com.google.gson.annotations.SerializedName

data class Latest(
    val id: Int,
    val popularity: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<Any>,
    @SerializedName("production_countries")
    val productionCountries: List<Any>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<Any>,
    val status: String,
    val original_title: String,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Int,
    @SerializedName("vote_count")
    val voteCount: Int
)