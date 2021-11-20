package com.felwal.android.widget.dialog

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogTextBinding
import com.felwal.android.util.string
import com.felwal.android.util.toast

const val NO_FLOAT_TEXT = -1f

private const val ARG_TEXT = "text"
private const val ARG_HINT = "hint"

class DecimalDialog : BaseDialog<DecimalDialog.DialogListener>() {

    // args
    private var text = 0f
    private lateinit var hint: String

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            text = getFloat(ARG_TEXT, 0f)
            hint = getString(ARG_HINT, "")
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = FwDialogTextBinding.inflate(inflater)
        binding.fwTf.editText!!.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL

        // widget
        binding.fwTf.editText!!.hint = hint
        if (text != NO_FLOAT_TEXT) binding.fwTf.editText!!.setText(text.toString())

        return builder.run {
            setView(binding.root)

            // title & message
            setTitleIfNonEmpty(title)
            setMessageIfNonEmpty(message)

            // buttons
            setPositiveButton(posBtnTxtRes) { _ ->
                try {
                    val input = binding.fwTf.editText!!.string.toFloat()
                    catchClassCast {
                        listener?.onDecimalDialogPositiveClick(input, dialogTag)
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
        fun onDecimalDialogPositiveClick(input: Float, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            text: Float = NO_FLOAT_TEXT,
            hint: String = "",
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): DecimalDialog = DecimalDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putFloat(ARG_TEXT, text)
                putString(ARG_HINT, hint)
            }
        }
    }
}

fun decimalDialog(
    title: String,
    message: String = "",
    text: Float = NO_FLOAT_TEXT,
    hint: String = "",
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): DecimalDialog = DecimalDialog.newInstance(title, message, text, hint, posBtnTxtRes, negBtnTxtRes, tag)