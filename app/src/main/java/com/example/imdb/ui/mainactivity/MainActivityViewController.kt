package com.example.imdb.ui.mainactivity

import java.util.Locale

class MainActivityViewController(dataController: IDataController) {
    init {
        dataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }
}