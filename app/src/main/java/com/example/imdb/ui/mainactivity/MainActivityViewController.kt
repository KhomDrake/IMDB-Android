package com.example.imdb.ui.mainactivity

import com.example.imdb.data.Repository
import java.util.Locale

class MainActivityViewController(repository: Repository) {
    init {
        repository.setupDatabase(Locale.getDefault().toLanguageTag())
    }
}