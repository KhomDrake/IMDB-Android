package com.example.imdb.data.entity.http


import com.google.gson.annotations.SerializedName

data class Session(
        @SerializedName("expires_at")
        val expiresAt: String,
        @SerializedName("guest_session_id")
        val guestSessionId: String,
        val success: Boolean
)