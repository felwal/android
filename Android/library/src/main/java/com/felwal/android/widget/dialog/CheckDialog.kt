package com.felwal.android.widget.dialog

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.util.asIndicesOfTrueBooleans
import com.felwal.android.util.firsts
import com.felwal.android.util.orEmpty
import com.felwal.android.util.seconds
import java.lang.ClassCastException

private const val ARG_ITEMS = "items"
private const val ARG_ITEM_STATES = "itemStates"

class CheckDialog : BaseDialog<CheckDialog.DialogListener>() {

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

    override fun buildDialog(): AlertDialog = builder.run {
        setTitle(title)

        setMultiChoiceItems(items, itemStates) { _, which, isChecked ->
            itemStates[which] = isChecked
        }
        setPositiveButton(posBtnTxtRes) { _, _ ->
            try {
                listener?.onCheckDialogPositiveClick(itemStates, dialogTag)
            }
            catch (e: ClassCastException) {
                // listener was not successfully safe-casted to L.
                // all we need to do here is prevent a crash if the listener was not implemented.
                Log.d("Dialog", "Conext was not successfully safe-casted as DialogListener")
            }
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onCheckDialogPositiveClick(checkedItems: BooleanArray, tag: String)
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
        ): CheckDialog = newInstance(
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
        ): CheckDialog = newInstance(
            title, message,
            items, checkedItems.asIndicesOfTrueBooleans(items.size),
            posBtnTxtRes, negBtnTxtRes, tag
        )

        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            items: Array<String>,
            itemStates: BooleanArray,
            @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): CheckDialog = CheckDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putStringArray(ARG_ITEMS, items)
                putBooleanArray(ARG_ITEM_STATES, itemStates)
            }
        }
    }
}

fun checkDialog(
    title: String,
    message: String = "",
    vararg items: Pair<String, Boolean>,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): CheckDialog =
    CheckDialog.newInstance(title, message, *items, posBtnTxtRes = posBtnTxtRes, negBtnTxtRes = negBtnTxtRes, tag = tag)

fun checkDialog(
    title: String,
    message: String = "",
    items: Array<String>,
    checkedItems: IntArray,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): CheckDialog = CheckDialog.newInstance(title, message, items, checkedItems, posBtnTxtRes, negBtnTxtRes, tag)

fun checkDialog(
    title: String,
    message: String = "",
    items: Array<String>,
    itemStates: BooleanArray,
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): CheckDialog = CheckDialog.newInstance(title, message, items, itemStates, posBtnTxtRes, negBtnTxtRes, tag)