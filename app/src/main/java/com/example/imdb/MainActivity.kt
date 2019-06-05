package com.example.imdb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.imdb.ui.home.HomeAppActivity
import com.example.imdb.ui.login.LoginActivity
import com.example.imdb.ui.mainactivity.MainActivityViewController
import org.koin.android.ext.android.inject

const val TAG_VINI = "vini"

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var guestButton: Button
    private val messageLoginButton = "Função indisponível"
    private val mainActivityViewController: MainActivityViewController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.button_login_home)
        guestButton = findViewById(R.id.guest)

        loginButton.setOnClickListener {
            val startNewActivity = Intent(this, LoginActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
//            Toast.makeText(this, messageLoginButton, Toast.LENGTH_SHORT).show()
        }

        guestButton.setOnClickListener {
            val startNewActivity = Intent(this, HomeAppActivity::class.java)
            ContextCompat.startActivity(this, startNewActivity, null)
        }
    }
}

