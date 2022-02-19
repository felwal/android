package me.felwal.android.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.text.Editable
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import me.felwal.android.R

const val ANIM_DURATION = 100L

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
        backgroundTintList = if (value != null) value.toColorStateList() else null
    }

// menu

@SuppressLint("RestrictedApi")
fun Menu.setOptionalIconsVisible(visible: Boolean) = (this as? MenuBuilder)?.setOptionalIconsVisible(visible)

val Menu.optionalItems
    @SuppressLint("RestrictedApi")
    get() = (this as? MenuBuilder)?.nonActionItems

@SuppressLint("RestrictedApi")
fun Menu.setOptionalIconsColor(@ColorInt color: Int) =
    optionalItems?.forEach { it.iconTintList = color.toColorStateList() }

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

// view

fun View.setItemRipple() =
    setBackgroundResource(context.getResIdByAttr(R.attr.selectableItemBackground))

fun View.setBorderlessItemRipple() =
    setBackgroundResource(context.getResIdByAttr(R.attr.selectableItemBackgroundBorderless))

fun View.setActionItemRipple() =
    setBackgroundResource(context.getResIdByAttr(android.R.attr.actionBarItemBackground))

fun View.canScrollUp() = canScrollVertically(-1)

fun View.canScrollDown() = canScrollVertically(1)

fun View.getActivity(): Activity? {
    var c = context
    while (c is ContextWrapper) {
        if (c is Activity) return c
        c = c.baseContext
    }
    return null
}

fun View.getChildAt(index: Int): View? = (this as? ViewGroup)?.getChildAt(index)

// misc

fun Layout.getStartOfLine(index: Int): Int = getLineStart(getLineForOffset(index))

val TabLayout.tabs get() = (getChildAt(0) as ViewGroup).children

fun TabLayout.setTabsBorderlessRipple() = tabs.forEach { it.setBorderlessItemRipple() }

/**
 * The root view of the layout set via [Activity.setContentView].
 *
 * Note: may or may not work on every device all the time.
 */
val Activity.contentView: View?
    get() = window.decorView.rootView.rootView
        .getChildAt(0)
        ?.getChildAt(1)
        ?.getChildAt(0)
        ?.getChildAt(1)
        ?.getChildAt(0)

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

fun View.crossfadeIn(
    duration: Long,
    toAlpha: Float = 1f,
    listener: ((Animator?) -> Unit)? = null
): ViewPropertyAnimator {
    alpha = 0f
    isVisible = true
    return animate().apply {
        alpha(toAlpha)
        setDuration(duration)
        setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                listener?.invoke(animation)
            }

            override fun onAnimationCancel(animation: Animator?) {
                listener?.invoke(animation)
            }
        })
        start()
    }
}

fun View.crossfadeOut(
    duration: Long,
    listener: ((Animator?) -> Unit)? = null
): ViewPropertyAnimator = animate().apply {
    alpha(0f)
    setDuration(duration)
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            isVisible = false
            listener?.invoke(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            isVisible = false
            listener?.invoke(animation)
        }
    })
    start()
}