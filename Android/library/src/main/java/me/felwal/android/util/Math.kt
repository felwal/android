package me.felwal.android.util

class MathUtils private constructor() {
    companion object {

        fun heaviside(value: Number): Int =
            if (value.toFloat() > 0) 1
            else 0

        fun heaviside(unity: Boolean?): Int =
            if (unity == true) 1
            else 0

        fun signum(value: Number): Int =
            if (value.toFloat() > 0) 1
            else if (value.toFloat() < 0) -1
            else 0

        fun signum(positive: Boolean?): Int =
            if (positive == true) 1
            else if (positive == false) -1
            else 0
    }
}

val Boolean?.sign: Int
    get() = MathUtils.signum(this)

fun Boolean?.toInt(): Int = MathUtils.heaviside(this)

fun Int.toBoolean(): Boolean = equals(1)