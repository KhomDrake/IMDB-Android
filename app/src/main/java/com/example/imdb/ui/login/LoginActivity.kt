package com.example.imdb.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.imdb.R
import com.example.imdb.TAG_VINI
import com.example.imdb.data.entity.http.LoginBody
import com.example.imdb.ui.home.HomeAppActivity
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val loginActivityViewController: LoginActivityViewController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEditText = findViewById(R.id.login_edit_text)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.button_login)

        loginButton.setOnClickListener {
            if(loginButton.text.isEmpty() && passwordEditText.text.isEmpty())
                Toast.makeText(this, "Login ou Senha Inválidos", Toast.LENGTH_SHORT).show()

            val login = loginButton.text.toString()
            val password = passwordEditText.text.toString()

            loginActivityViewController.validLogin(LoginBody(password, login, requestToken = "")) { result, message ->
                if(result) {
                    val startNewActivity = Intent(this, HomeAppActivity::class.java)
                    ContextCompat.startActivity(this, startNewActivity, null)
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
