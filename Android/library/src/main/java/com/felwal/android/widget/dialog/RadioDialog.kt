package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R

private const val ARG_LABELS = "labels"
private const val ARG_CHECKED_INDEX = "checkedIndex"

class RadioDialog : BaseDialog<RadioDialog.DialogListener>() {

    // args
    private lateinit var labels: Array<out String>
    private var checkedIndex = 0

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            checkedIndex = getInt(ARG_CHECKED_INDEX, 0).coerceIn(0, labels.size)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        // title & message
        setTitleIfNonEmpty(title)
        setMessageIfNonEmpty(message)

        // items
        setSingleChoiceItems(labels, checkedIndex) { dialog, index ->
            catchClassCast {
                listener?.onRadioDialogItemClick(index, dialogTag)
            }
            dialog.cancel()
        }

        // button
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onRadioDialogItemClick(checkedIndex: Int, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            labels: List<String>,
            checkedIndex: Int,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String
        ): RadioDialog = RadioDialog().apply {
            arguments = putBaseBundle(title, message, NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putStringArray(ARG_LABELS, labels.toTypedArray())
                putInt(ARG_CHECKED_INDEX, checkedIndex)
            }
        }
    }
}

fun radioDialog(
    title: String,
    message: String = "",
    labels: List<String>,
    checkedIndex: Int,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String
): RadioDialog = RadioDialog.newInstance(title, message, labels, checkedIndex, negBtnTxtRes, tag)