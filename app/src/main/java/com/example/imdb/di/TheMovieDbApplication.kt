package com.example.imdb.di

import android.app.Application
import org.koin.android.ext.android.startKoin

class TheMovieDbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this.applicationContext, listOf(movieDbKoinModule))
    }
}