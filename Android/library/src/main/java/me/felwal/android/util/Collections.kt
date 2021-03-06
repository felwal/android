package me.felwal.android.util

// inclusion

fun <E> MutableCollection<E>.toggleInclusion(element: E): Boolean =
    if (contains(element)) remove(element) else add(element)

fun <E> MutableList<E>.replace(oldElement: E, newElement: E): E =
    set(indexOf(oldElement), newElement)

// repeat

fun <E> List<E>.repeated(times: Int): List<E> =
    toMutableList().also { ml -> repeat(times - 1) { ml.addAll(this) } }

inline fun <reified E> Array<E>.repeated(times: Int): Array<E> =
    toMutableList().also { ml -> repeat(times - 1) { ml.addAll(this) } }.toTypedArray()

fun IntArray.repeated(times: Int): IntArray =
    toMutableList().also { ml -> repeat(times - 1) { ml.addAll(toTypedArray()) } }.toIntArray()

// remove

fun <E> MutableCollection<E>.removeAll(): Boolean = removeAll(this)

fun <E> MutableList<E>.removeAll(range: IntRange) =
    range.reversed().forEach { removeAt(it) }

fun <E> MutableList<E>.removeAllFrom(index: Int) =
    removeAll(index until size)

fun <E> MutableList<E>.removeAllTo(index: Int) =
    removeAll(0..index)

operator fun IntArray.minus(element: Int): IntArray =
    (toMutableList() - element).toIntArray()

operator fun BooleanArray.minus(element: Boolean): BooleanArray =
    (toMutableList() - element).toBooleanArray()

inline operator fun <reified E> Array<E>.minus(element: E): Array<E> =
    (toMutableList() - element).toTypedArray()

// indices

fun <E> List<E>.indicesOf(sublist: List<E>): List<Int> =
    sublist.map { indexOf(it) }

fun <E> Array<E>.indicesOf(subarray: Array<E>): IntArray =
    subarray.map { indexOf(it) }.toIntArray()

fun List<Int>.asIndicesOfTruths(size: Int): List<Boolean> {
    val itemStates = MutableList(size) { false }
    forEach { itemStates[it] = true }
    return itemStates
}

fun IntArray.asIndicesOfTruths(size: Int = this.size): BooleanArray {
    val itemStates = BooleanArray(size) { false }
    forEach { itemStates[it] = true }
    return itemStates
}

fun List<Boolean>.toIndicesOfTruths(): List<Int> =
    mapIndexed { index, b -> if (b) index else null }.filterNotNull()

fun BooleanArray.toIndicesOfTruths(): IntArray =
    mapIndexed { index, b -> if (b) index else null }.filterNotNull().toIntArray()

// filter

fun <E> List<E>.filter(include: BooleanArray): List<E> =
    filterIndexed { index, _ -> include[index] }

// fill/crop

fun <E> MutableCollection<E?>.fillUp(value: E?, toSize: Int) =
    repeat(toSize - size) { add(value) }

fun <E> MutableCollection<E?>.crop(toSize: Int) =
    repeat(size - toSize) { remove(elementAt(size - 1)) }

fun <E> MutableCollection<E?>.cropUp(value: E?, toSize: Int) {
    if (size < toSize) fillUp(value, toSize)
    else if (size > toSize) crop(toSize)
}

// logic

inline fun <T, R> Iterable<T>.common(value: (T) -> R): R? {
    if (this is Collection && isEmpty()) return null

    val firstValue = value(elementAt(0))
    for (element in this) if (value(element) != firstValue) return null
    return firstValue
}

// convert

inline fun <reified E> MutableList<E>.toNullable(): MutableList<E?> =
    mutableListOf(*toTypedArray())

fun BooleanArray?.orEmpty(): BooleanArray = this ?: BooleanArray(0)

fun IntArray?.orEmpty(): IntArray = this ?: IntArray(0)

val <A, B> Array<out Pair<A, B>>.firsts: List<A>
    get() = map { it.first }

val <A, B> Array<out Pair<A, B>>.seconds: List<B>
    get() = map { it.second }