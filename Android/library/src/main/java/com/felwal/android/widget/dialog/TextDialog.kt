package com.felwal.android.widget.dialog

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogTextBinding
import com.felwal.android.util.string

private const val ARG_TEXT = "text"
private const val ARG_HINT = "hint"

class TextDialog : BaseDialog<TextDialog.DialogListener>() {

    // args
    private lateinit var text: String
    private lateinit var hint: String

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            text = getString(ARG_TEXT, "")
            hint = getString(ARG_HINT, "")
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = FwDialogTextBinding.inflate(inflater)
        binding.fwEt.inputType = EditorInfo.TYPE_CLASS_TEXT

        // widget
        binding.fwEt.setText(text)
        binding.fwEt.hint = hint

        return builder.run {
            setView(binding.root)

            // title & message
            setTitleIfNonEmpty(title)
            setMessageIfNonEmpty(message)

            // buttons
            setPositiveButton(posBtnTxtRes) { _ ->
                val input = binding.fwEt.string.trim { it == ' ' }
                catchClassCast {
                    listener?.onTextDialogPositiveClick(input, dialogTag)
                }
            }
            setCancelButton(negBtnTxtRes)

            show()
        }
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onTextDialogPositiveClick(input: String, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            text: String = "",
            hint: String = "",
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): TextDialog = TextDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putString(ARG_TEXT, text)
                putString(ARG_HINT, hint)
            }
        }
    }
}

fun textDialog(
    title: String,
    message: String = "",
    text: String = "",
    hint: String = "",
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): TextDialog = TextDialog.newInstance(title, message, text, hint, posBtnTxtRes, negBtnTxtRes, tag)