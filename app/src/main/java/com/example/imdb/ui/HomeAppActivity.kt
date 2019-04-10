package com.example.imdb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imdb.R

class HomeAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//            navegationdrawer
        setContentView(R.layout.activity_home_themoviedb)
    }
}
