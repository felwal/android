package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R

class UnaryDialog : BaseDialog<UnaryDialog.DialogListener>() {

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        // bundle is empty but for base bundle
    }

    override fun buildDialog(): AlertDialog = builder.run {
        // title & message
        setTitleIfNonEmpty(title)
        setMessageIfNonEmpty(message)

        // button
        setPositiveButton(posBtnTxtRes) { _, _ ->
            catchClassCast {
                listener?.onUnaryDialogClick(dialogTag)
            }
        }

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onUnaryDialogClick(tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            @StringRes btnTxtRes: Int = R.string.dialog_btn_ok,
            tag: String
        ): UnaryDialog = UnaryDialog().apply {
            arguments = putBaseBundle(title, message, btnTxtRes, tag = tag)
        }
    }
}

fun unaryDialog(
    title: String,
    message: String = "",
    @StringRes btnTxtRes: Int = R.string.dialog_btn_ok,
    tag: String
): UnaryDialog = UnaryDialog.newInstance(title, message, btnTxtRes, tag)