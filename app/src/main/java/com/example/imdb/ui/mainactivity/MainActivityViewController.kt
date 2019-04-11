package com.example.imdb.ui.mainactivity

import android.content.Context
import com.example.imdb.data.DataController
import java.util.Locale

class MainActivityViewController {

    init {
        DataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }

    fun createDatabase(ctx: Context) {
        DataController.createDatabase(ctx)
    }

}