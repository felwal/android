package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.felwal.android.databinding.FwDialogListBinding
import com.felwal.android.widget.control.DialogOption
import com.felwal.android.widget.control.RadioGroupOption
import com.felwal.android.widget.control.getRadioGroupOption
import com.felwal.android.widget.control.inflateRadioGroup
import com.felwal.android.widget.control.putRadioGroupOption

private const val ARG_RADIO_GROUP = "radioGroup"

class RadioDialog : SingleChoiceDialog() {

    // args
    private lateinit var radioOption: RadioGroupOption

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            radioOption = getRadioGroupOption(ARG_RADIO_GROUP)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option) {
            listener?.onSingleChoiceDialogItemSelected(radioOption.checkedIndex, option.tag, option.passValue)
        }

        inflateRadioGroup(radioOption, binding.fwLl) { index ->
            // there is no positive button; make the dialog simple, i.e. dismiss on item click
            if (option.posBtnTxtRes == NO_RES) {
                catchClassCast {
                    listener?.onSingleChoiceDialogItemSelected(index, option.tag, option.passValue)
                }
                dialog?.cancel()
            }
            else radioOption.checkedIndex = index
        }

        show().apply {
            fixScrollingDialogCustomPanelPadding()
        }
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            radioOption: RadioGroupOption
        ): RadioDialog = RadioDialog().apply {
            arguments = putBaseBundle(option).apply {
                putRadioGroupOption(ARG_RADIO_GROUP, radioOption)
            }
        }
    }
}

fun radioDialog(
    option: DialogOption,
    radioOption: RadioGroupOption
): RadioDialog = RadioDialog.newInstance(option, radioOption)