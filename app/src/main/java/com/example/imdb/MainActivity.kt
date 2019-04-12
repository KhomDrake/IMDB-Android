package com.example.imdb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.imdb.ui.home.HomeAppActivity
import com.example.imdb.ui.mainactivity.MainActivityViewController

enum class MovieCategory {
    Zero,
    Latest,
    NowPlaying,
    Popular,
    TopRated,
    Upcoming,
    Recommendation
}

const val TAG_VINI = "vini"

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var guestButton: Button
    private val messageLoginButton = "Função indisponível"
    private lateinit var mainActivityViewController: MainActivityViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewController = MainActivityViewController()

        mainActivityViewController.createDatabase(this)

        loginButton = findViewById(R.id.login)
        guestButton = findViewById(R.id.guest)

        loginButton.setOnClickListener {
            Toast.makeText(this, messageLoginButton, Toast.LENGTH_SHORT).show()
        }

        guestButton.setOnClickListener {
            val startNewActivity = Intent(this, HomeAppActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }
    }
}
