package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog as AndroidXAlertDialog
import com.felwal.android.R

private const val ARG_PASS_VALUE = "passValue"
private const val ARG_NEUTRAL_BUTTON_RES = "neutralButtonText"

class AlertDialog : BaseDialog<AlertDialog.DialogListener>() {

    // args
    private var passValue: String? = null
    @StringRes private var neuBtnTxtRes: Int = NO_RES

    override val hasButtons get() = super.hasButtons || neuBtnTxtRes != NO_RES

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            passValue = getString(ARG_PASS_VALUE, null)
            neuBtnTxtRes = getInt(ARG_NEUTRAL_BUTTON_RES, NO_RES)
        }
    }

    override fun buildDialog(): AndroidXAlertDialog = builder.run {
        // title & message
        setTitleIfNonEmpty(title)
        setMessageIfNonEmpty(message)

        // buttons
        setPositiveButton(posBtnTxtRes) { _ ->
            catchClassCast {
                listener?.onAlertDialogPositiveClick(passValue, dialogTag)
            }
        }
        setNeutralButton(neuBtnTxtRes) { _ ->
            catchClassCast {
                listener?.onAlertDialogNeutralClick(passValue, dialogTag)
            }
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onAlertDialogPositiveClick(passValue: String?, tag: String)

        fun onAlertDialogNeutralClick(passValue: String?, tag: String) {}
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            @StringRes neuBtnTxtRes: Int? = null,
            tag: String,
            passValue: String? = null
        ): AlertDialog = AlertDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putString(ARG_PASS_VALUE, passValue)
                putInt(ARG_NEUTRAL_BUTTON_RES, neuBtnTxtRes ?: NO_RES)
            }
        }
    }
}

fun alertDialog(
    title: String,
    message: String = "",
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    @StringRes neuBtnTxtRes: Int? = null,
    tag: String,
    passValue: String? = null
): AlertDialog = AlertDialog.newInstance(title, message, posBtnTxtRes, negBtnTxtRes, neuBtnTxtRes, tag, passValue)