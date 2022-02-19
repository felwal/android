package me.felwal.android.util

import android.util.Log

class FLog(private val tag: String) {

    fun v(msg: String) = Log.v(tag, msg)

    fun d(msg: String) = Log.d(tag, msg)

    fun i(msg: String) = Log.i(tag, msg)

    fun w(msg: String) = Log.w(tag, msg)

    fun e(msg: String) = Log.e(tag, msg)
}

internal val log = FLog("Felwal")