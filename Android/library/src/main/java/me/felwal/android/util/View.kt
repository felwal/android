package me.felwal.android.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.ContextWrapper
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import me.felwal.android.R

const val ANIM_DURATION = 100L

// visibility

fun invisibleOrGone(invisible: Boolean): Int = if (invisible) View.INVISIBLE else View.GONE

fun View.hideOrRemove(hide: Boolean) {
    visibility = invisibleOrGone(hide)
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