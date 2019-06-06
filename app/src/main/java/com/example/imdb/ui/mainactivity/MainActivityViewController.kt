package com.example.imdb.ui.mainactivity

import android.util.Log
import com.example.imdb.TAG_VINI
import com.example.imdb.data.Repository
import java.util.Locale

class MainActivityViewController(private val repository: Repository) {
    init {
        repository.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun getSessionId(response: (String) -> Unit) {
        Log.i(TAG_VINI, "lkdasjdlksajdlkasjdlska")
        repository.getSessionId(response)
    }
}