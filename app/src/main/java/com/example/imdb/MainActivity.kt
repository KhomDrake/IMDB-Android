package com.example.imdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imdb.ui.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewControllerMainActivity.setupView(
            findViewById(R.id.movies),
            findViewById(R.id.latest),
            findViewById(R.id.popular),
            findViewById(R.id.toprated),
            findViewById(R.id.upcoming)
        )
    }
}
