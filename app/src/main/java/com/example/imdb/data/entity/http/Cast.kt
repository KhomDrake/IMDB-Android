package com.example.imdb.data.entity.http

import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("cast_id")
    val castId: Int,
    val character: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    @SerializedName("profile_path")
    val profilePath: String?,
    val error: Boolean
)