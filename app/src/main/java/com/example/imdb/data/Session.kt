package com.example.imdb.data

object Session {
    private var sessionId: String = ""
    var sessionIdName = "session_id"

    fun getSessionId() : String {
        return sessionId
    }

    fun setSessionId(newSessionId: String) {
        sessionId = newSessionId
    }
}