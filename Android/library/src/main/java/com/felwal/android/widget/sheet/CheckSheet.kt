package com.felwal.android.widget.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.felwal.android.R
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.asIndicesOfTruths
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.dialog.CheckDialog

private const val ARG_LABELS = "labels"
private const val ARG_ITEM_STATES = "itemStates"
private const val ARG_ICONS = "icons"

class CheckSheet : MultiChoiceSheet() {

    private lateinit var labels: Array<out String>
    private lateinit var itemStates: BooleanArray
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            itemStates = getBooleanArray(ARG_ITEM_STATES).orEmpty().copyOf()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        setMultiChoiceItems(labels, itemStates, iconsRes, binding.fwLl) { index, isChecked, ->
            itemStates[index] = isChecked
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        catchClassCast {
            listener?.onMultiChoiceSheetItemsSelected(itemStates, sheetTag, passValue)
        }
        super.onDismiss(dialog)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            labels: Array<String>,
            checkedIndices: IntArray,
            @DrawableRes icons: IntArray? = null,
            tag: String,
            passValue: String? = null
        ): CheckSheet = newInstance(
            title, labels, checkedIndices.asIndicesOfTruths(labels.size),
            icons, tag, passValue
        )

        @JvmStatic
        fun newInstance(
            title: String,
            labels: Array<String>,
            itemStates: BooleanArray,
            @DrawableRes icons: IntArray? = null,
            tag: String,
            passValue: String? = null
        ): CheckSheet = CheckSheet().apply {
            arguments = putBaseBundle(title, tag, passValue).apply {
                putStringArray(ARG_LABELS, labels)
                putBooleanArray(ARG_ITEM_STATES, itemStates)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun checkSheet(
    title: String,
    labels: Array<String>,
    checkedIndices: IntArray,
    @DrawableRes icons: IntArray? = null,
    tag: String,
    passValue: String? = null
): CheckSheet = CheckSheet.newInstance(title, labels, checkedIndices, icons, tag, passValue)

fun checkSheet(
    title: String,
    labels: Array<String>,
    itemStates: BooleanArray,
    @DrawableRes icons: IntArray? = null,
    tag: String,
    passValue: String? = null
): CheckSheet = CheckSheet.newInstance(title, labels, itemStates, icons, tag, passValue)