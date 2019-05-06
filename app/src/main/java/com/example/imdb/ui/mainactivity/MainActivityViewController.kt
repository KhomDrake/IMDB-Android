package com.example.imdb.ui.mainactivity

import com.example.imdb.data.IDataController
import java.util.Locale

class MainActivityViewController(dataController: IDataCon addtroller) {
    init {
        dataController.setupDatabase(Locale.getDefault().toLanguageTag())
    }
}