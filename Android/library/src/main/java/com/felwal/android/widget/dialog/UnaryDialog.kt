package com.felwal.android.widget.dialog

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import java.lang.ClassCastException

class UnaryDialog : BaseDialog<UnaryDialog.DialogListener>() {

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        // bundle is empty but for base bundle
    }

    override fun buildDialog(): AlertDialog = builder.run {
        setTitle(title)
        if (message != "") setMessage(message)

        setPositiveButton(posBtnTxtRes) { _, _ ->
            try {
                listener?.onUnaryDialogClick(dialogTag)
            }
            catch (e: ClassCastException) {
                // listener was not successfully safe-casted to L.
                // all we need to do here is prevent a crash if the listener was not implemented.
                Log.d("Dialog", "Conext was not successfully safe-casted as DialogListener")
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