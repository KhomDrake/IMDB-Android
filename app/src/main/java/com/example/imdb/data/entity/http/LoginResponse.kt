package com.example.imdb.data.entity.http


import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("expires_at")
        val expiresAt: String,
        @SerializedName("request_token")
        val requestToken: String,
        val success: Boolean
)