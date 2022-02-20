package me.felwal.android.fragment.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import me.felwal.android.databinding.FwDialogTextBinding
import me.felwal.android.util.string
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.InputOption
import me.felwal.android.widget.control.getInputOption
import me.felwal.android.widget.control.putInputOption

const val NO_INT_TEXT = -1
const val NO_LONG_TEXT = -1L

private const val ARG_INPUT = "input"

class InputDialog : BaseDialog<InputDialog.DialogListener>() {

    // args
    private lateinit var inputOption: InputOption

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            inputOption = getInputOption(ARG_INPUT)
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = FwDialogTextBinding.inflate(inflater)
        binding.fwTf.editText!!.inputType = inputOption.inputType

        // widget
        binding.fwTf.editText!!.setText(inputOption.text)
        binding.fwTf.editText!!.hint = inputOption.hint

        return builder.run {
            setView(binding.root)

            setDialogOptions(option) {
                val input = binding.fwTf.editText!!.string.trim { it == ' ' }
                listener?.onInputDialogPositiveClick(input, option.tag, option.passValue)
            }

            show()
        }
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onInputDialogPositiveClick(input: String, tag: String, passValue: String?)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            inputOption: InputOption
        ): InputDialog = InputDialog().apply {
            arguments = putBaseBundle(option).apply {
                putInputOption(ARG_INPUT, inputOption)
            }
        }
    }
}

fun inputDialog(
    option: DialogOption,
    inputOption: InputOption
): InputDialog = InputDialog.newInstance(option, inputOption)