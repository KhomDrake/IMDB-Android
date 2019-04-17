package com.example.imdb.ui.mainactivity

import com.example.imdb.data.DataController
import java.util.Locale

class MainActivityViewController(dataController: DataController) {
    init {
        dataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }
}