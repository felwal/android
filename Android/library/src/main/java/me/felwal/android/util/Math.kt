package me.felwal.android.util

class MathUtils private constructor() {
    companion object {

        fun heaviside(value: Number) = if (value.toFloat() > 0) 1 else 0

        fun heaviside(unity: Boolean?) = if (unity == true) 1 else 0

        fun signum(value: Number) = if (value.toFloat() > 0) 1 else if (value.toFloat() < 0) -1 else 0

        fun signum(positive: Boolean?) = if (positive == true) 1 else if (positive == false) -1 else 0
    }
}

fun Boolean?.toInt() = MathUtils.heaviside(this)

val Boolean?.sign get() = MathUtils.signum(this)

fun Int.toBoolean() = equals(1)