package me.felwal.android.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar

// toast

fun Context.toast(text: String, long: Boolean = false) =
    Toast.makeText(this, text, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        .show()

fun Context.toast(@StringRes textRes: Int, long: Boolean = false) =
    toast(getString(textRes, long))

fun Context.toastLog(tag: String, msg: String, e: Exception? = null) {
    toast(msg, true)
    Log.d(tag, msg, e)
    e?.printStackTrace()
}

fun Context.toastLog(tag: String, @StringRes msgRes: Int, e: Exception? = null) =
    toastLog(tag, getString(msgRes), e)

fun Context.tryToast(text: String, long: Boolean = false) {
    try {
        toast(text, long)
    }
    catch (e: RuntimeException) {
        Log.e("ContextUtils", "toast message: $text", e)
    }
}

fun Context.tryToast(@StringRes textRes: Int, long: Boolean = false) =
    tryToast(getString(textRes, long))

fun Context.tryToastLog(tag: String, msg: String, e: Exception? = null) {
    tryToast(msg, true)
    Log.d(tag, msg, e)
    e?.printStackTrace()
}

fun Context.tryToastLog(tag: String, @StringRes msgRes: Int, e: Exception? = null) =
    tryToastLog(tag, getString(msgRes), e)

suspend fun Context.coToast(text: String, long: Boolean = false) = withUI {
    toast(text, long)
}

suspend fun Context.coToast(@StringRes textRes: Int, long: Boolean = false) =
    coToast(getString(textRes, long))

suspend fun Context.coToastLog(tag: String, msg: String, e: Exception? = null) {
    coToast(msg, true)
    Log.d(tag, msg, e)
    e?.printStackTrace()
}

suspend fun Context.coToastLog(tag: String, @StringRes msgRes: Int, e: Exception? = null) =
    coToastLog(tag, getString(msgRes), e)

fun Activity.uiToast(text: String, long: Boolean = false) = runOnUiThread {
    toast(text, long)
}

fun Activity.uiToast(@StringRes textRes: Int, long: Boolean = false) =
    uiToast(getString(textRes, long))

fun Activity.uiToastLog(tag: String, msg: String, e: Exception? = null) = runOnUiThread {
    uiToast(msg, true)
    Log.d(tag, msg, e)
    e?.printStackTrace()
}

fun Activity.uiToastLog(tag: String, @StringRes msgRes: Int, e: Exception? = null) =
    uiToastLog(tag, getString(msgRes), e)

// snackbar

fun Context.snackbar(
    view: View,
    text: String,
    long: Boolean? = false,
    actionText: String? = null,
    listener: ((View) -> Unit)? = null
) = Snackbar.make(
    this, view, text,
    if (long == null) Snackbar.LENGTH_INDEFINITE else if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
)
    .apply { if (listener != null) setAction(actionText ?: "Action", listener) }
    .show()

fun View.snackbar(text: String, long: Boolean = true, actionText: String = "", action: ((it: View) -> Unit)? = null) =
    Snackbar.make(this, text, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
        .setAction(actionText, action)
        .show()

// popup menu

fun <C> C.popup(
    anchor: View,
    @MenuRes menuRes: Int
) where C : Context, C : PopupMenu.OnMenuItemClickListener =
    PopupMenu(this, anchor).apply {
        menuInflater.inflate(menuRes, menu)
        setOnMenuItemClickListener(this@popup)
        show()
    }

fun Context.popup(
    anchor: View,
    @MenuRes menuRes: Int,
    listener: ((MenuItem) -> Boolean)? = null
) = PopupMenu(this, anchor).apply {
    menuInflater.inflate(menuRes, menu)
    setOnMenuItemClickListener(listener)
    show()
}

//

@SuppressLint("RestrictedApi")
fun Menu.setOptionalIconsVisible(visible: Boolean) = (this as? MenuBuilder)?.setOptionalIconsVisible(visible)

val Menu.optionalItems
    @SuppressLint("RestrictedApi")
    get() = (this as? MenuBuilder)?.nonActionItems

@SuppressLint("RestrictedApi")
fun Menu.setOptionalIconsColor(@ColorInt color: Int) =
    optionalItems?.forEach { it.iconTintList = color.toColorStateList() }