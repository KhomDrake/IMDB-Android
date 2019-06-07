package com.example.imdb.network

import android.util.Log
import com.example.imdb.TAG_VINI
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url().encodedPath()
        val response = chain.proceed(request)
        val code = response.code()

        Log.i(TAG_VINI, "Begin ----")
        Log.i(TAG_VINI, request.method())
        Log.i(TAG_VINI, path)
        Log.i(TAG_VINI, response.message())
        Log.i(TAG_VINI, code.toString())
        Log.i(TAG_VINI, "End ----")

        return response
    }
}