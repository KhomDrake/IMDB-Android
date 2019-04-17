package com.example.imdb.dependencyinjection

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.standalone.KoinComponent

class TheMovieDbApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(movieDbKoinModule))
    }

}