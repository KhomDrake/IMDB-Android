package com.example.imdb.data.entity.http.tv.season_episode


import com.example.imdb.data.entity.http.tv.Crew
import com.google.gson.annotations.SerializedName

data class Episodes(
    @SerializedName("air_date")
    val airDate: String,
    val crew: List<Crew>,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("guest_stars")
    val guestStars: List<GuestStar>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("production_code")
    val productionCode: String,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("still_path")
    val stillPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)