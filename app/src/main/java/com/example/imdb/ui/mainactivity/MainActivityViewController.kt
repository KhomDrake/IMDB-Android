package com.example.imdb.ui.mainactivity

import com.example.imdb.data.Repository
import java.util.Locale

class MainActivityViewController(private val repository: Repository) {
    init {
        repository.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun getSessionId(response: (String) -> Unit) {
        repository.getSessionId(response)
    }
}