package com.felwal.android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// coroutine

suspend fun <T> withUI(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.Main, block)

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)

// experiment

operator fun ((Int) -> Any).get(x: Int) = invoke(x)

fun <T> Any.to() = this as T