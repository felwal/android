package me.felwal.android.util

object MathUtils  {

    const val GOLDEN_RATIO = 1.618033988749894

    // heaviside

    fun heavisideRight(value: Number): Int =
        if (value.toFloat() >= 0) 1
        else 0

    fun heavisideLeft(value: Number): Int =
        if (value.toFloat() > 0) 1
        else 0

    fun heavisideHalf(value: Number): Float =
        when {
            value.toFloat() > 0 -> 1f
            value.toFloat() < 0 -> 0f
            else -> 0.5f
        }

    fun heavisideRight(unity: Boolean?): Int =
        if (unity != false) 1
        else 0

    fun heavisideLeft(unity: Boolean?): Int =
        if (unity == true) 1
        else 0

    fun heavisideHalf(value: Boolean?): Float =
        when (value) {
            true -> 1f
            false -> 0f
            null -> 0.5f
        }

    // signum

    fun signum(value: Number): Int =
        when {
            value.toFloat() > 0 -> 1
            value.toFloat() < 0 -> -1
            else -> 0
        }

    fun signum(positive: Boolean?): Int =
        when (positive) {
            true -> 1
            false -> -1
            else -> 0
        }

    // golden ratio

    fun goldenRatioSmallPart(of: Int): Int =
        (of / (1 + GOLDEN_RATIO)).toInt()

    fun goldenRatioLargePart(of: Int): Int =
        (of * GOLDEN_RATIO / (1 + GOLDEN_RATIO)).toInt()

}

val Boolean?.sign: Int
    get() = MathUtils.signum(this)

fun Boolean.toInt(): Int = MathUtils.heavisideLeft(this)

fun Int.toBoolean(): Boolean = MathUtils.heavisideLeft(this) == 1