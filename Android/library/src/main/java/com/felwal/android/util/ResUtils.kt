package com.felwal.android.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.appcompat.content.res.AppCompatResources

// get res

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? = AppCompatResources.getDrawable(this, id)

fun Context.getBoolean(@BoolRes id: Int): Boolean = resources.getBoolean(id)

fun Context.getDimension(@DimenRes id: Int): Float = resources.getDimension(id)

fun Context.getInteger(@IntegerRes id: Int): Int = resources.getInteger(id)

fun Context.getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String =
    if (formatArgs.isEmpty()) resources.getQuantityString(id, quantity, quantity)
    else resources.getQuantityString(id, quantity, *formatArgs)

fun Context.getStringArray(@ArrayRes id: Int): Array<String> = resources.getStringArray(id)

fun Context.getIntegerArray(@ArrayRes id: Int): IntArray = resources.getIntArray(id)

fun Context.getResIdArray(@ArrayRes id: Int): IntArray {
    val typedArray: TypedArray = resources.obtainTypedArray(id)
    val resIds = IntArray(typedArray.length()) { i ->
        typedArray.getResourceId(i, 0)
    }
    typedArray.recycle()
    return resIds
}

// get attr res

/**
 * Gets resource id from attribute [attr].
 */
fun Context.getResIdByAttr(@AttrRes attr: Int): Int {
    val attrs = intArrayOf(attr)
    val typedArray = obtainStyledAttributes(attrs)
    val resId = typedArray.getResourceId(0, 0)
    typedArray.recycle()
    return resId

    // a different method, not sure if one is better than the other
    /*val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return = typedValue.resourceId*/
}

@ColorInt
fun Context.getColorByAttr(@AttrRes attr: Int): Int = getColor(getResIdByAttr(attr))

fun Context.getDrawableByAttr(@AttrRes attr: Int): Drawable? = getDrawableCompat(getResIdByAttr(attr))

fun Context.getBooleanByAttr(@AttrRes attr: Int): Boolean = getBoolean(getResIdByAttr(attr))

fun Context.getDimensionByAttr(@AttrRes attr: Int): Float = getDimension(getResIdByAttr(attr))

fun Context.getIntegerByAttr(@AttrRes attr: Int): Int = getInteger(getResIdByAttr(attr))

fun Context.getStringByAttr(@AttrRes attr: Int): String = getString(getResIdByAttr(attr))

fun Context.getQuantityStringByAttr(@AttrRes attr: Int, quantity: Int, vararg formatArgs: Any?): String =
    getQuantityString(getResIdByAttr(attr), quantity, formatArgs)

fun Context.getStringArrayByAttr(@AttrRes attr: Int): Array<String> = getStringArray(getResIdByAttr(attr))

fun Context.getIntegerArrayByAttr(@AttrRes attr: Int): IntArray = getIntegerArray(getResIdByAttr(attr))

fun Context.getResIdArrayByAttr(@AttrRes attr: Int): IntArray = getResIdArray(getResIdByAttr(attr))

// combination

fun Context.getDrawableCompatWithTint(@DrawableRes id: Int, @AttrRes colorAttr: Int): Drawable? =
    getDrawableCompat(id)?.withTint(getColorByAttr(colorAttr))

fun Context.getDrawableCompatWithFilter(@DrawableRes id: Int, @AttrRes colorAttr: Int): Drawable? =
    getDrawableCompat(id)?.withFilter(getColorByAttr(colorAttr))

fun Context.getDrawableByAttrWithTint(@AttrRes id: Int, @AttrRes colorAttr: Int): Drawable? =
    getDrawableByAttr(id)?.withTint(getColorByAttr(colorAttr))

fun Context.getDrawableByAttrWithFilter(@AttrRes id: Int, @AttrRes colorAttr: Int): Drawable? =
    getDrawableByAttr(id)?.withFilter(getColorByAttr(colorAttr))

// drawable

fun Drawable.withTint(@ColorInt tint: Int): Drawable = mutate().also { setTint(tint) }

fun Drawable.withFilter(@ColorInt tint: Int): Drawable = mutate().also { setColorFilter(tint, PorterDuff.Mode.SRC_IN) }

fun Drawable.toBitmap(): Bitmap? {
    (this as? BitmapDrawable)?.bitmap?.let { return it }

    val bitmap =
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            // Single color bitmap will be created of 1x1 pixel
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
        else {
            Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)

    return bitmap
}