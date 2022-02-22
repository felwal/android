package me.felwal.android.fragment.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import me.felwal.android.databinding.FwDialogListBinding
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.RadioGroupOption
import me.felwal.android.widget.control.getRadioGroupOption
import me.felwal.android.widget.control.inflateRadioGroup
import me.felwal.android.widget.control.putRadioGroupOption

private const val ARG_RADIO_GROUP = "radioGroup"

class RadioDialog : SingleChoiceDialog() {

    // args
    private lateinit var radioOption: RadioGroupOption

    private lateinit var binding: FwDialogListBinding

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            radioOption = getRadioGroupOption(ARG_RADIO_GROUP)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option) {
            listener?.onSingleChoiceDialogItemSelected(radioOption.checkedIndex, option.tag, option.passValue)
        }

        inflateRadioGroup(radioOption, binding.fwLl) { index ->
            // there is no positive button; make the dialog simple, i.e. dismiss on item click
            if (option.posBtnTxtRes == NO_RES) {
                listener?.onSingleChoiceDialogItemSelected(index, option.tag, option.passValue)
                dismiss()
            }
            else radioOption.checkedIndex = index
        }

        show().apply {
            fixScrollingDialogCustomPanelPadding()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putRadioGroupOption(ARG_RADIO_GROUP, radioOption)
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