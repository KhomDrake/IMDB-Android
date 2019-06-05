package com.example.imdb.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.imdb.R
import com.example.imdb.TAG_VINI

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEditText = findViewById(R.id.login_edit_text)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.button_login)

        loginButton.setOnClickListener {
            Log.i(TAG_VINI, loginEditText.text.toString())
            Log.i(TAG_VINI, passwordEditText.text.toString())
        }
    }
}
