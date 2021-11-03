package com.felwal.android.lang

enum class Trilean {
    NEG,
    NEU,
    POS
}

fun Trilean.toBoolean(): Boolean? = when (this) {
    Trilean.NEG -> false
    Trilean.NEU -> null
    Trilean.POS -> true
}

fun Boolean?.toTrilean(): Trilean = if (this == null) Trilean.NEU else if (this) Trilean.POS else Trilean.NEG