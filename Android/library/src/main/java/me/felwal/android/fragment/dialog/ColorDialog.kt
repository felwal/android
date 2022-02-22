package me.felwal.android.fragment.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import me.felwal.android.R
import me.felwal.android.databinding.FwDialogColorBinding
import me.felwal.android.databinding.FwItemDialogColorBinding
import me.felwal.android.util.backgroundTint
import me.felwal.android.util.getDrawableByAttrWithTint
import me.felwal.android.util.orEmpty
import me.felwal.android.widget.control.DialogOption

private const val ARG_COLORS = "colors"
private const val ARG_CHECKED_INDEX = "checkedIndex"

class ColorDialog : SingleChoiceDialog() {

    // args
    @ColorInt private lateinit var colors: IntArray
    private var checkedIndex: Int = 0

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
        setDividers(binding.fwGv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option) {
            listener?.onSingleChoiceDialogItemSelected(checkedIndex, option.tag, option.passValue)
        }

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
                ivColor.setImageDrawable(
                    if (position != checkedIndex) null
                    else context.getDrawableByAttrWithTint(R.attr.fw_colorDialogSelectedIcon, R.attr.colorSurface)
                )

                // set listener
                ivColor.setOnClickListener {
                    listener?.onSingleChoiceDialogItemSelected(position, option.tag, option.passValue)
                    dismiss()
                }

                return clItem
            }
        }

        show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putIntArray(ARG_COLORS, colors)
            putInt(ARG_CHECKED_INDEX, checkedIndex)
        }
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            @ColorInt colors: IntArray,
            checkedIndex: Int? = null,
        ): ColorDialog = ColorDialog().apply {
            arguments = putBaseBundle(option).apply {
                putIntArray(ARG_COLORS, colors)
                putInt(ARG_CHECKED_INDEX, checkedIndex ?: NULL_INT)
            }
        }
    }
}

fun colorDialog(
    option: DialogOption,
    @ColorInt colors: IntArray,
    checkedIndex: Int? = null,
): ColorDialog = ColorDialog.newInstance(option, colors, checkedIndex)