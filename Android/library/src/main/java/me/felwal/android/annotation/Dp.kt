package me.felwal.android.annotation

import androidx.annotation.Dimension

/**
 * Denotes that an integer parameter, field or method return value is expected
 * to represent a dp dimension.
 */
@MustBeDocumented @kotlin.annotation.Retention(AnnotationRetention.BINARY) @Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
) @Dimension(unit = Dimension.DP)
annotation class Dp