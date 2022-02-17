package com.felwal.android.util

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px
import com.felwal.android.annotation.Dp
import com.felwal.android.annotation.Sp
import kotlin.math.round
import kotlin.math.roundToInt

// string

fun CharSequence.split(
    vararg delimiters: String,
    lowerLimit: Int,
    upperLimit: Int = 0,
    ignoreCase: Boolean = false
): List<String?> = split(*delimiters, ignoreCase = ignoreCase, limit = upperLimit)
    .toMutableList()
    .toNullable()
    .apply { fillUp(null, lowerLimit) }

fun CharSequence.coerceSubstring(startIndex: Int, endIndex: Int): String =
    substring(startIndex.coerceIn(0, length), endIndex.coerceIn(0, length))

fun String.findAll(string: String, startIndex: Int = 0, ignoreCase: Boolean = false): List<Int> {
    val key = if (ignoreCase) string.lowercase() else string
    val content = if (ignoreCase) lowercase() else this
    val indices = mutableListOf<Int>()
    val keyLen = key.length

    for (index in startIndex.coerceAtLeast(0)..(content.length - keyLen)) {
        if (content.substring(index, index + keyLen) == key) indices.add(index)
    }

    return indices
}

// int

fun Int.toColorStateList() = ColorStateList.valueOf(this)

@ColorInt
fun Int.multiplyAlphaComponent(@FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt().coerceIn(0, 255)
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}

val Int.pxToDp: Int @Dp get() = round(toFloat().pxToDp).toInt()

val Int.pxTpSp: Int @Sp get() = round(toFloat().pxToSp).toInt()

val Int.dpToPx: Int @Px get() = round(toFloat().dpTpPx).toInt()

val Int.spToPx: Int @Px get() = round(toFloat().spToPx).toInt()

val Float.pxToDp: Float @Dp get() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, Resources.getSystem().displayMetrics)

val Float.pxToSp: Float @Sp get() =
    this / Resources.getSystem().displayMetrics.scaledDensity

val Float.dpTpPx: Float @Px get() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Float.spToPx: Float @Px get() =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)