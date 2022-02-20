package me.felwal.android.widget.dialog

import android.os.Bundle
import me.felwal.android.widget.control.DialogOption
import androidx.appcompat.app.AlertDialog as AndroidXAlertDialog

class AlertDialog : BaseDialog<AlertDialog.DialogListener>() {

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {}

    override fun buildDialog(): AndroidXAlertDialog = builder.run {
        setDialogOptions(option, {
            listener?.onAlertDialogNeutralClick(option.tag, option.passValue)
        }, {
            listener?.onAlertDialogPositiveClick(option.tag, option.passValue)
        })

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onAlertDialogPositiveClick(tag: String, passValue: String?)

        fun onAlertDialogNeutralClick(tag: String, passValue: String?) {}
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
        ): AlertDialog = AlertDialog().apply {
            arguments = putBaseBundle(option)
        }
    }
}

fun alertDialog(
    option: DialogOption,
): AlertDialog = AlertDialog.newInstance(option)