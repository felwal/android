package com.felwal.android.widget.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogColorBinding
import com.felwal.android.databinding.FwItemDialogColorBinding
import com.felwal.android.util.backgroundTint
import com.felwal.android.util.getDrawableCompatWithTint
import com.felwal.android.util.orEmpty

private const val ARG_COLORS = "colors"
private const val ARG_CHECKED_INDEX = "checkedIndex"

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
        val binding = FwDialogColorBinding.inflate(inflater)
        setView(binding.root)

        // title
        setTitleIfNonEmpty(title)

        // widget
        setDividers(binding.fwGv, binding.fwVDividerTop, binding.fwVDividerBottom)

        // items
        binding.fwGv.adapter = object : ArrayAdapter<Int>(requireContext(), 0, colors.toList()) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                // find views
                val clItem = convertView
                    ?: FwItemDialogColorBinding.inflate(layoutInflater, binding.fwGv, false).root
                val ivColor = clItem.findViewById<ImageView>(R.id.fw_iv_color)

                // set color
                ivColor.backgroundTint = getItem(position)

                // set checked drawable
                if (position == checkedIndex) {
                    val icon = context.getDrawableCompatWithTint(R.drawable.fw_ic_check_24, R.attr.colorSurface)
                    ivColor.setImageDrawable(icon)
                }
                else ivColor.setImageDrawable(null)

                // set listener
                ivColor.setOnClickListener {
                    catchClassCast {
                        listener?.onSingleChoiceDialogItemSelected(position, dialogTag)
                    }
                    dialog?.cancel()
                }

                return clItem
            }
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
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
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
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): ColorDialog = ColorDialog.newInstance(title, colors, checkedIndex, negBtnTxtRes, tag)