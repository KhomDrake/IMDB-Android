package com.example.imdb.data.entity.http

import com.google.gson.annotations.SerializedName

data class APIError(
    @SerializedName("status_message")
    val statusMessage: String,
    @SerializedName("status_code")
    val statusCode: Int
)