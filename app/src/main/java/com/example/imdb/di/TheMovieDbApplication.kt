package com.example.imdb.di

import android.app.Application
import android.util.Log
import com.example.imdb.TAG_VINI
import org.koin.android.ext.android.startKoin

class TheMovieDbApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG_VINI, "asd")
        startKoin(this, listOf(movieDbKoinModule))
    }

}