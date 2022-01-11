package com.felwal.android.widget.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.felwal.android.R
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.dialog.NO_RES
import com.felwal.android.widget.dialog.RadioDialog

private const val ARG_LABELS = "labels"
private const val ARG_CHECKED_INDEX = "checkedIndex"
private const val ARG_ICONS = "icons"

class RadioSheet : SingleChoiceSheet() {

    private lateinit var labels: Array<out String>
    private var checkedIndex = 0
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            checkedIndex = getInt(ARG_CHECKED_INDEX, 0).coerceIn(0, labels.size)
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        setSingleChoiceItems(labels, checkedIndex, iconsRes, binding.fwLl) { selectedIndex ->
            checkedIndex = selectedIndex
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        catchClassCast {
            listener?.onSingleChoiceSheetItemSelected(checkedIndex, sheetTag, passValue)
        }
        super.onDismiss(dialog)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            labels: List<String>,
            checkedIndex: Int,
            @DrawableRes icons: IntArray? = null,
            tag: String,
            passValue: String? = null
        ): RadioSheet = RadioSheet().apply {
            arguments = putBaseBundle(title, tag, passValue).apply {
                putStringArray(ARG_LABELS, labels.toTypedArray())
                putInt(ARG_CHECKED_INDEX, checkedIndex)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun radioSheet(
    title: String,
    labels: List<String>,
    checkedIndex: Int,
    @DrawableRes icons: IntArray? = null,
    tag: String,
    passValue: String? = null
): RadioSheet = RadioSheet.newInstance(title, labels, checkedIndex, icons, tag, passValue)