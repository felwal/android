package com.felwal.android.util

import android.util.Log

private const val LOG_TAG = "Felwal"

internal fun v(msg: String) = Log.v(LOG_TAG, msg)

internal fun d(msg: String) = Log.d(LOG_TAG, msg)

internal fun i(msg: String) = Log.i(LOG_TAG, msg)

internal fun w(msg: String) = Log.w(LOG_TAG, msg)

internal fun e(msg: String) = Log.e(LOG_TAG, msg)