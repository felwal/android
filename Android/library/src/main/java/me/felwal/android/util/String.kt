package me.felwal.android.util

fun CharSequence.split(
    vararg delimiters: String,
    lowerLimit: Int,
    upperLimit: Int = 0,
    ignoreCase: Boolean = false
): List<String?> =split(*delimiters, ignoreCase = ignoreCase, limit = upperLimit)
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