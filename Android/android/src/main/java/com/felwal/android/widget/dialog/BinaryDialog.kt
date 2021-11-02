package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R

private const val ARG_PASS_VALUE = "passValue"

class BinaryDialog : BaseDialog<BinaryDialog.DialogListener>() {

    // args
    private var passValue: String? = null

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            passValue = getString(ARG_PASS_VALUE, null)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        setTitle(title)
        if (message != "") setMessage(message)

        setPositiveButton(posBtnTxtRes) { _, _ ->
            listener.onBinaryDialogPositiveClick(passValue, dialogTag)
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onBinaryDialogPositiveClick(passValue: String?, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
            tag: String,
            passValue: String? = null
        ): BinaryDialog = BinaryDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putString(ARG_PASS_VALUE, passValue)
            }
        }
    }
}

fun binaryDialog(
    title: String,
    message: String = "",
    @StringRes posBtnTxtRes: Int = R.string.dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.dialog_btn_cancel,
    tag: String,
    passValue: String? = null
): BinaryDialog = BinaryDialog.newInstance(title, message, posBtnTxtRes, negBtnTxtRes, tag, passValue)