package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.util.asIndicesOfTruths
import com.felwal.android.util.firsts
import com.felwal.android.util.orEmpty
import com.felwal.android.util.seconds

private const val ARG_LABELS = "labels"
private const val ARG_ITEM_STATES = "itemStates"

class CheckDialog : MultiChoiceDialog() {

    // args
    private lateinit var labels: Array<out String>
    private lateinit var itemStates: BooleanArray

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            itemStates = getBooleanArray(ARG_ITEM_STATES).orEmpty()
        }

        if (labels.size != itemStates.size) {
            throw IndexOutOfBoundsException("labels and itemStates must have equal size")
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        // title
        setTitleIfNonEmpty(title)

        // items
        setMultiChoiceItems(labels, itemStates) { _, index, isChecked ->
            itemStates[index] = isChecked
        }

        // buttons
        setPositiveButton(posBtnTxtRes) { _ ->
            catchClassCast {
                listener?.onMultiChoiceDialogItemsSelected(itemStates, dialogTag)
            }
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            vararg items: Pair<String, Boolean>,
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): CheckDialog = newInstance(
            title,
            items.firsts.toTypedArray(), items.seconds.toBooleanArray(),
            posBtnTxtRes, negBtnTxtRes, tag
        )

        @JvmStatic
        fun newInstance(
            title: String,
            labels: Array<String>,
            checkedIndices: IntArray,
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): CheckDialog = newInstance(
            title,
            labels, checkedIndices.asIndicesOfTruths(labels.size),
            posBtnTxtRes, negBtnTxtRes, tag
        )

        @JvmStatic
        fun newInstance(
            title: String,
            labels: Array<String>,
            itemStates: BooleanArray,
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): CheckDialog = CheckDialog().apply {
            arguments = putBaseBundle(title, "", posBtnTxtRes, negBtnTxtRes, tag).apply {
                putStringArray(ARG_LABELS, labels)
                putBooleanArray(ARG_ITEM_STATES, itemStates)
            }
        }
    }
}

fun checkDialog(
    title: String,
    vararg items: Pair<String, Boolean>,
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): CheckDialog =
    CheckDialog.newInstance(title, *items, posBtnTxtRes = posBtnTxtRes, negBtnTxtRes = negBtnTxtRes, tag = tag)

fun checkDialog(
    title: String,
    labels: Array<String>,
    checkedIndices: IntArray,
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): CheckDialog = CheckDialog.newInstance(title, labels, checkedIndices, posBtnTxtRes, negBtnTxtRes, tag)

fun checkDialog(
    title: String,
    labels: Array<String>,
    itemStates: BooleanArray,
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): CheckDialog = CheckDialog.newInstance(title, labels, itemStates, posBtnTxtRes, negBtnTxtRes, tag)