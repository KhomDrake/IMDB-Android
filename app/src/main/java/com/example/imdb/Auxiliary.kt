package com.example.imdb

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

object Auxiliary {
    private val urlImg = "https://image.tmdb.org/t/p/w200"
    private val imgNotFound = "https://uae.microless.com/cdn/no_image.jpg"
    const val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"

    fun getPath(path: String?) : String {
        if(path == "null" || path == "" || path == null)
            return imgNotFound
        else
            return urlImg + path
    }

    fun getVerticalLinearLayoutManager(ctx: Context) = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
}