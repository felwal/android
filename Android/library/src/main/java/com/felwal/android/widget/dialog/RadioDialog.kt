package com.felwal.android.widget.dialog

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import java.lang.ClassCastException

private const val ARG_ITEMS = "items"
private const val ARG_CHECKED_ITEM = "checkedItem"

class RadioDialog : BaseDialog<RadioDialog.DialogListener>() {

    // args
    private lateinit var items: Array<out String>
    private var checkedItem = 0

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
            checkedItem = getInt(ARG_CHECKED_ITEM, 0).coerceIn(0, items.size)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        setTitle(title)
        if (message != "") setMessage(message)

        setSingleChoiceItems(items, checkedItem) { dialog, which ->
            try {
                listener?.onRadioDialogItemClick(which, dialogTag)
            }
            catch (e: ClassCastException) {
                // listener was not successfully safe-casted to L.
                // all we need to do here is prevent a crash if the listener was not implemented.
                Log.d("Dialog", "Conext was not successfully safe-casted as DialogListener")
            }
            dialog.cancel()
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onRadioDialogItemClick(checkedItem: Int, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            items: List<String>,
            checkedItem: Int,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): RadioDialog = RadioDialog().apply {
            arguments = putBaseBundle(title, message, NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putStringArray(ARG_ITEMS, items.toTypedArray())
                putInt(ARG_CHECKED_ITEM, checkedItem)
            }
        }
    }
}

fun radioDialog(
    title: String,
    message: String = "",
    items: List<String>,
    checkedItem: Int,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): RadioDialog = RadioDialog.newInstance(title, message, items, checkedItem, negBtnTxtRes, tag)