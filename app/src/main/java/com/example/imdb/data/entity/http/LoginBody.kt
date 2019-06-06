package com.example.imdb.data.entity.http


import com.google.gson.annotations.SerializedName

data class LoginBody(
        val password: String,
        @SerializedName("request_token")
        val requestToken: String,
        val username: String
)