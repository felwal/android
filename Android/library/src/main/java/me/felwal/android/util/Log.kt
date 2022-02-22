package me.felwal.android.util

import android.util.Log

class FLog(private val tag: String) {

    fun v(msg: String): Int = Log.v(tag, msg)

    fun d(msg: String): Int = Log.d(tag, msg)

    fun i(msg: String): Int = Log.i(tag, msg)

    fun w(msg: String): Int = Log.w(tag, msg)

    fun e(msg: String): Int = Log.e(tag, msg)
}

internal val log = FLog("Felwal")