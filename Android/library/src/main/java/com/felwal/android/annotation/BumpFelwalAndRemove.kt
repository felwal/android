package com.felwal.android.annotation

import androidx.annotation.Dimension

/**
 * Denotes that something should be moved to this library.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE) @Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
)
annotation class BumpFelwalAndRemove