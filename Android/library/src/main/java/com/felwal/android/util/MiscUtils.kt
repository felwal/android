package com.felwal.android.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// lifecycle

inline fun <reified A : AppCompatActivity> AppCompatActivity.launchActivity() {
    val intent = Intent(this, A::class.java)
    startActivity(intent)
}

// coroutines

suspend fun <T> withUI(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.Main, block)

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)