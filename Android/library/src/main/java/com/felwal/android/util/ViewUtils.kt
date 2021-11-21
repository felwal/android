package com.felwal.android.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.felwal.android.R
import com.google.android.material.snackbar.Snackbar

const val ANIM_DURATION = 100

// visibility

fun invisibleOrGone(invisible: Boolean): Int = if (invisible) View.INVISIBLE else View.GONE

fun View.hideOrRemove(hide: Boolean) {
    visibility = invisibleOrGone(hide)
}

fun TextView.setTextRemoveIfEmpty(value: String) {
    text = value
    isGone = text == ""
}

// color

/**
 * Shorthand for [View.getBackgroundTintList]
 */
var View.backgroundTint: Int?
    @ColorInt get() = backgroundTintList?.defaultColor
    set(@ColorInt value) {
        backgroundTintList = if (value != null) ColorStateList.valueOf(value) else null
    }

// snackbar

fun View.snackbar(text: String, long: Boolean = true, actionText: String = "", action: ((it: View) -> Unit)? = null) =
    Snackbar.make(this, text, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
        .setAction(actionText, action)
        .show()

// menu

@SuppressLint("RestrictedApi")
fun Menu.setOptionalIconsVisible(visible: Boolean) = (this as? MenuBuilder)?.setOptionalIconsVisible(visible)

val MenuItem.searchView: SearchView get() = actionView as SearchView

val SearchView.closeIcon: ImageView get() = findViewById(androidx.appcompat.R.id.search_close_btn)

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

fun Layout.getStartOfLine(index: Int): Int = getLineStart(getLineForOffset(index))

fun View.enableRipple() =
    setBackgroundResource(context.getResIdByAttr(R.attr.selectableItemBackground))

fun View.enableActionItemRipple() =
    setBackgroundResource(context.getResIdByAttr(android.R.attr.actionBarItemBackground))

fun View.canScrollUp() = canScrollVertically(-1)

fun View.canScrollDown() = canScrollVertically(1)

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

// anim

fun View.crossfadeIn(toAlpha: Float) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(toAlpha)
        .setDuration(ANIM_DURATION.toLong())
        .setListener(null)
}

fun View.crossfadeIn() = crossfadeIn(1f)

fun View.crossfadeOut() {
    animate()
        .alpha(0f)
        .setDuration(ANIM_DURATION.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}