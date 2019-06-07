package com.example.imdb.data.entity.http


import com.google.gson.annotations.SerializedName

data class LoginBody(
        val password: String,
        val username: String,
        @SerializedName("request_token")
        var requestToken: String
)