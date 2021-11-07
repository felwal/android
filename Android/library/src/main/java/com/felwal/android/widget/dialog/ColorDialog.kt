package com.felwal.android.widget.dialog

import android.os.Bundle
import android.widget.TableRow
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.DialogColorBinding
import com.felwal.android.databinding.ItemDialogColorBinding
import com.felwal.android.util.backgroundTint
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.util.isPortrait
import com.felwal.android.util.orEmpty

private const val ARG_COLORS = "colors"
private const val ARG_CHECKED_INDEX = "checkedIndex"

private const val COLUMN_COUNT_PORTRAIT = 4
private const val COLUMN_COUNT_LANDSCAPE = 5

class ColorDialog : SingleChoiceDialog() {

    // args
    @ColorInt private lateinit var colors: IntArray
    private var checkedIndex = 0

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            colors = getIntArray(ARG_COLORS).orEmpty()
            checkedIndex = getInt(ARG_CHECKED_INDEX, 0).coerceIn(-1, colors.size)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = DialogColorBinding.inflate(inflater)
        setView(binding.root)

        // title
        setTitleIfNonEmpty(title)

        // widget
        setDividers(binding.sv, binding.vDividerTop, binding.vDividerBottom)

        // items
        var tr = TableRow(binding.tl.context)
        binding.tl.addView(tr)
        val columnCount = if (context.isPortrait) COLUMN_COUNT_PORTRAIT else COLUMN_COUNT_LANDSCAPE
        for ((i, color) in colors.withIndex()) {
            // inflate row
            if (i != 0 && i % columnCount == 0) {
                tr = TableRow(context)
                binding.tl.addView(tr)
            }

            // inflate item
            val itemBinding = ItemDialogColorBinding.inflate(inflater, tr, false)
            itemBinding.ivColor.backgroundTint = color

            // set checked drawable
            if (i == checkedIndex) {
                val icon = context.getDrawableCompat(R.drawable.ic_check_24, R.attr.colorSurface)
                itemBinding.ivColor.setImageDrawable(icon)
            }

            itemBinding.ivColor.setOnClickListener {
                catchClassCast {
                    listener?.onSingleChoiceDialogItemSelected(i, dialogTag)
                }
                dialog?.cancel()
            }

            tr.addView(itemBinding.root)
        }

        // button
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            @ColorInt colors: IntArray,
            checkedIndex: Int? = null,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): ColorDialog = ColorDialog().apply {
            arguments = putBaseBundle(title, "", NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putIntArray(ARG_COLORS, colors)
                putInt(ARG_CHECKED_INDEX, checkedIndex ?: NULL_INT)
            }
        }
    }
}

fun colorDialog(
    title: String,
    @ColorInt colors: IntArray,
    checkedIndex: Int? = null,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): ColorDialog = ColorDialog.newInstance(title, colors, checkedIndex, negBtnTxtRes, tag)