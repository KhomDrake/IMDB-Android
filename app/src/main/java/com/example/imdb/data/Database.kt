package com.example.imdb.data

import com.example.imdb.entity.Result
import com.example.imdb.network.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Database {

    private val apiKey = "ed84e9c8c38d4d0a8f3adaa5ba324145"
    private var latest: MutableList<Result> = mutableListOf()
    private var nowPlaying: MutableList<Result> = mutableListOf()
    private var popular: MutableList<Result> = mutableListOf()
    private var topRated: MutableList<Result> = mutableListOf()
    private var upcoming: MutableList<Result> = mutableListOf()
    private var firstRequest = true

//    private fun loadingFirstDatas(language: String)
//    {
//        api.apply {
//            getLatest(apiKey, language).enqueue(requestResponse<Result> {
//
//            })
//        }
//    }

    private fun <T>requestResponse(funResponse: (body: T) -> Unit) = object: Callback<T> {
        override fun onFailure(call: Call<T?>, t: Throwable) = Unit

        override fun onResponse(call: Call<T?>, response: Response<T?>) {
            response.body()?.apply(funResponse)
        }
    }
}