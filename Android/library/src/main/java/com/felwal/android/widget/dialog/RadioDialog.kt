package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R

private const val ARG_LABELS = "labels"
private const val ARG_CHECKED_INDEX = "checkedIndex"

class RadioDialog : SingleChoiceDialog() {

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
        // title
        setTitleIfNonEmpty(title)

        // items
        setSingleChoiceItems(labels, checkedIndex) { dialog, index ->
            // there is no positive button; make the dialog simple, i.e. dismiss on item click
            if (posBtnTxtRes == NO_RES) {
                catchClassCast {
                    listener?.onSingleChoiceDialogItemSelected(index, dialogTag)
                }
                dialog.cancel()
            }
            else checkedIndex = index
        }

        // buttons
        setPositiveButton(posBtnTxtRes) { _ ->
            catchClassCast {
                listener?.onSingleChoiceDialogItemSelected(checkedIndex, dialogTag)
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
            labels: List<String>,
            checkedIndex: Int,
            @StringRes posBtnTxtRes: Int? = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): RadioDialog = RadioDialog().apply {
            arguments = putBaseBundle(title, "", posBtnTxtRes ?: NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putStringArray(ARG_LABELS, labels.toTypedArray())
                putInt(ARG_CHECKED_INDEX, checkedIndex)
            }
        }
    }
}

fun radioDialog(
    title: String,
    labels: List<String>,
    checkedIndex: Int,
    @StringRes posBtnTxtRes: Int? = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): RadioDialog = RadioDialog.newInstance(title, labels, checkedIndex, posBtnTxtRes, negBtnTxtRes, tag)