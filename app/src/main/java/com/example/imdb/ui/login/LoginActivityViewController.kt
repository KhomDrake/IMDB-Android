package com.example.imdb.ui.login

import com.example.imdb.data.Repository
import com.example.imdb.data.entity.http.LoginBody

class LoginActivityViewController(private val repository: Repository) {

    fun validLogin(body: LoginBody, response: (Boolean, String) -> Unit) {
        repository.validLogin(body, response)
    }
}