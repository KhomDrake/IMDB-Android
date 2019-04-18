package com.example.imdb.auxiliary

import kotlinx.coroutines.*

fun io(work: suspend (() -> Unit)): Job = CoroutineScope(Dispatchers.IO).launch { work() }

fun <T: Any> ioThenDefault(work: suspend (() -> T?), callback: ((T?) -> Unit)): Job =
    CoroutineScope(Dispatchers.Default).launch {
        val data = CoroutineScope(Dispatchers.IO).async rt@ {
            return@rt work()
        }.await()
        callback(data)
    }