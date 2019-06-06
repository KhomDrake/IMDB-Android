package com.example.imdb.data.entity.http


import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)