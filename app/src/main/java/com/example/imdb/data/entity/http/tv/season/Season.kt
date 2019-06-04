package com.example.imdb.data.entity.http.tv.season


import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("air_date")
    val airDate: String,
    val episodes: List<Episode>,
    @SerializedName("_id")
    val _id: String,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("season_number")
    val seasonNumber: Int
)