package me.felwal.android.util

import android.text.Editable
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

// menu

val MenuItem.searchView: SearchView get() = actionView as SearchView

val SearchView.closeIcon: ImageView get() = findViewById(androidx.appcompat.R.id.search_close_btn)

// textview

fun TextView.setTextRemoveIfEmpty(value: String) {
    text = value
    isGone = text == ""
}

// edittext

val EditText.string: String get() = text.toString()

fun Editable.copy(): Editable = Editable.Factory.getInstance().newEditable(this)

/**
 * Creates a copy of the [EditText]'s [Editable] ([EditText.getText]), executes the [block]
 * and then reassignes with [EditText.setText].
 */
inline fun EditText.updateEditable(block: Editable.() -> Unit) {
    val textCopy = text.copy()
    textCopy.block()
    text = textCopy
}

fun EditText.selectStart() = setSelection(0)

fun EditText.selectEnd() = setSelection(string.length)

fun EditText.coerceSelection(start: Int, stop: Int? = null) =
    if (stop == null) setSelection(start.coerceIn(0, length()))
    else setSelection(start.coerceIn(0, length()), stop.coerceIn(0, length()))

/**
 * Makes the EditText wrap multiple lines
 * while at the same time preventing the keyboard Enter button.
 */
fun EditText.makeMultilinePreventEnter() = apply {
    isSingleLine = true
    setHorizontallyScrolling(false) // allow wrapping
    maxLines = 100 // allow expanding
}

//

val TabLayout.tabs get() = (getChildAt(0) as ViewGroup).children

fun TabLayout.setTabsBorderlessRipple() = tabs.forEach { it.setBorderlessItemRipple() }

fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.submitListKeepScroll(
    list: List<T>,
    manager: RecyclerView.LayoutManager?,
    commitCallback: (() -> Unit)? = null
) {
    //  save state
    val recyclerViewState = manager?.onSaveInstanceState()

    // submit items
    submitList(list) {
        // restore state
        recyclerViewState?.let {
            manager.onRestoreInstanceState(it)
        }

        commitCallback?.invoke()
    }
}