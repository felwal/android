package com.felwal.android.widget.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.widget.NestedScrollView
import com.felwal.android.R
import com.felwal.android.databinding.DialogColorBinding
import com.felwal.android.databinding.ItemDialogColorBinding
import com.felwal.android.util.backgroundTint
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.util.isPortrait
import com.felwal.android.util.orEmpty
import java.lang.ClassCastException

private const val ARG_ITEMS = "items"
private const val ARG_CHECKED_ITEM = "checkedItem"

private const val COLUMN_COUNT_PORTRAIT = 4
private const val COLUMN_COUNT_LANDSCAPE = 5

class ColorDialog : BaseDialog<ColorDialog.DialogListener>() {

    // args
    @ColorInt private lateinit var items: IntArray
    private var checkedItem = 0

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getIntArray(ARG_ITEMS).orEmpty()
            checkedItem = getInt(ARG_CHECKED_ITEM, 0).coerceIn(-1, items.size)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = DialogColorBinding.inflate(inflater)
        setView(binding.root)
        setTitle(title)
        if (message != "") setMessage(message)

        // scrollview borders
        setDividers(binding.sv, binding.vDividerTop, binding.vDividerBottom)

        val columnCount = if (context.isPortrait) COLUMN_COUNT_PORTRAIT else COLUMN_COUNT_LANDSCAPE

        var tr = TableRow(binding.tl.context)
        binding.tl.addView(tr)

        for ((i, color) in items.withIndex()) {
            // inflate row
            if (i != 0 && i % columnCount == 0) {
                tr = TableRow(context)
                binding.tl.addView(tr)
            }

            // inflate item
            val itemBinding = ItemDialogColorBinding.inflate(inflater, tr, false)
            itemBinding.ivColor.backgroundTint = color

            // set checked drawable
            if (i == checkedItem) {
                val icon = context.getDrawableCompat(R.drawable.ic_check_24, R.attr.colorSurface)
                itemBinding.ivColor.setImageDrawable(icon)
            }

            itemBinding.ivColor.setOnClickListener {
                try {
                    listener?.onColorDialogItemClick(i, dialogTag)
                }
                catch (e: ClassCastException) {
                    // listener was not successfully safe-casted to L.
                    // all we need to do here is prevent a crash if the listener was not implemented.
                    Log.d("Dialog", "Conext was not successfully safe-casted as DialogListener")
                }
                dialog?.cancel()
            }

            tr.addView(itemBinding.root)
        }

        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onColorDialogItemClick(checkedItem: Int, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            @ColorInt items: IntArray,
            checkedItem: Int? = null,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): ColorDialog = ColorDialog().apply {
            arguments = putBaseBundle(title, message, NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putIntArray(ARG_ITEMS, items)
                putInt(ARG_CHECKED_ITEM, checkedItem ?: NULL_INT)
            }
        }
    }
}

fun colorDialog(
    title: String,
    message: String = "",
    @ColorInt items: IntArray,
    checkedItem: Int? = null,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): ColorDialog = ColorDialog.newInstance(title, message, items, checkedItem, negBtnTxtRes, tag)