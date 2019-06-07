package com.example.imdb.data.entity.http

import com.google.gson.annotations.SerializedName

data class CreateSession(
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String)