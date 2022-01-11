package com.felwal.android.widget.dialog

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogTextBinding
import com.felwal.android.util.string
import com.felwal.android.util.toast

const val NO_INT_TEXT = -1

private const val ARG_TEXT = "text"
private const val ARG_HINT = "hint"

class NumberDialog : BaseDialog<NumberDialog.DialogListener>() {

    // args
    private var text = 0
    private lateinit var hint: String

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            text = getInt(ARG_TEXT, 0)
            hint = getString(ARG_HINT, "")
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = FwDialogTextBinding.inflate(inflater)
        binding.fwTf.editText!!.inputType = EditorInfo.TYPE_CLASS_NUMBER

        // widget
        binding.fwTf.editText!!.hint = hint
        if (text != NO_INT_TEXT) binding.fwTf.editText!!.setText(text.toString())

        return builder.run {
            setView(binding.root)

            // title & message
            setTitleIfNonEmpty(title)
            setMessageIfNonEmpty(message)

            // buttons
            setPositiveButton(posBtnTxtRes) { _ ->
                try {
                    val input = binding.fwTf.editText!!.string.toInt()
                    catchClassCast {
                        listener?.onNumberDialogPositiveClick(input, dialogTag, passValue)
                    }
                }
                catch (e: NumberFormatException) {
                    activity?.toast(R.string.fw_toast_e_input)
                }
            }
            setCancelButton(negBtnTxtRes)

            show()
        }
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onNumberDialogPositiveClick(input: Int, tag: String, passValue: String?)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            text: Int = NO_INT_TEXT,
            hint: String = "",
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String,
            passValue: String? = null
        ): NumberDialog = NumberDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag, passValue).apply {
                putInt(ARG_TEXT, text)
                putString(ARG_HINT, hint)
            }
        }
    }
}

fun numberDialog(
    title: String,
    message: String = "",
    text: Int = NO_INT_TEXT,
    hint: String = "",
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String,
    passValue: String? = null
): NumberDialog = NumberDialog.newInstance(title, message, text, hint, posBtnTxtRes, negBtnTxtRes, tag, passValue)