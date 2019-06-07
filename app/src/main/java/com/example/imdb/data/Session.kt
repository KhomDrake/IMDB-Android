package com.example.imdb.data

object Session {
    private var sessionId: String = ""
    var sessionIdName = "session_id"
    private var isLoginAsGuest = false

    fun getSessionId() = sessionId


    fun getIsLoginAsGuest() = isLoginAsGuest

    fun loginAsGuest() {
        isLoginAsGuest = true
    }

    fun loginAsUser() {
        isLoginAsGuest = false
    }

    fun setSessionId(newSessionId: String) {
        sessionId = newSessionId
    }
}