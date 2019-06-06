package com.example.imdb.data

import android.util.Log
import com.example.imdb.TAG_VINI

object Session {
    private var sessionId: String = ""
    var sessionIdName = "session_id"

    fun getSessionId() : String {
        Log.i(TAG_VINI, "djsakhaskjhdas")
        return sessionId
    }

    fun setSessionId(newSessionId: String) {
        sessionId = newSessionId
    }
}