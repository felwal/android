package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.ComponentChipBinding
import com.felwal.android.databinding.DialogChipBinding
import com.felwal.android.util.asIndicesOfTrueBooleans
import com.felwal.android.util.firsts
import com.felwal.android.util.orEmpty
import com.felwal.android.util.seconds
import com.google.android.material.chip.Chip

private const val ARG_ITEMS = "items"
private const val ARG_ITEM_STATES = "itemStates"

class ChipDialog : BaseDialog<ChipDialog.DialogListener>() {

    // args
    private lateinit var items: Array<out String>
    private lateinit var itemStates: BooleanArray

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
            itemStates = getBooleanArray(ARG_ITEM_STATES).orEmpty()
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = DialogChipBinding.inflate(inflater)

        return builder.run {
            setView(binding.root)
            setTitle(title)
            binding.tv.text = message

            // set chips
            val chipGroup = binding.cg
            for (i in items.indices) {
                val chip: Chip = ComponentChipBinding.inflate(inflater, chipGroup, false).root
                chipGroup.addView(chip)
                chip.text = items[i]
                chip.isChecked = itemStates[i]

                chip.setOnCheckedChangeListener { _, isChecked ->
                    itemStates[i] = isChecked
                }
            }

            setPositiveButton(posBtnTxtRes) { _, _ ->
                listener.onChipDialogPositiveClick(itemStates, dialogTag)
            }
            setCancelButton(negBtnTxtRes)

            show()
        }
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onChipDialogPositiveClick(checkedItems: BooleanArray, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            vararg items: Pair<String, Boolean>,
            @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): ChipDialog = newInstance(
            title, message,
            items.firsts.toTypedArray(), items.seconds.toBooleanArray(),
            posBtnTxtRes, negBtnTxtRes, tag
        )

        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            items: Array<String>,
            checkedItems: IntArray,
            @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): ChipDialog {
            return newInstance(
                title, message,
                items, checkedItems.asIndicesOfTrueBooleans(items.size),
                posBtnTxtRes, negBtnTxtRes, tag
            )
        }

        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            items: Array<String>,
            itemStates: BooleanArray,
            @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): ChipDialog = ChipDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putStringArray(ARG_ITEMS, items)
                putBooleanArray(ARG_ITEM_STATES, itemStates)
            }
        }
    }
}

fun chipDialog(
    title: String,
    message: String = "",
    vararg items: Pair<String, Boolean>,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): ChipDialog =
    ChipDialog.newInstance(title, message, *items, posBtnTxtRes = posBtnTxtRes, negBtnTxtRes = negBtnTxtRes, tag = tag)

fun chipDialog(
    title: String,
    message: String = "",
    items: Array<String>,
    checkedItems: IntArray,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): ChipDialog = ChipDialog.newInstance(title, message, items, checkedItems, posBtnTxtRes, negBtnTxtRes, tag)

fun chipDialog(
    title: String,
    message: String = "",
    items: Array<String>,
    itemStates: BooleanArray,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): ChipDialog = ChipDialog.newInstance(title, message, items, itemStates, posBtnTxtRes, negBtnTxtRes, tag)