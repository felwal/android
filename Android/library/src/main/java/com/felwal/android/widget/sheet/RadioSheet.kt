package com.felwal.android.widget.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.control.RadioGroupOption
import com.felwal.android.widget.control.SheetOption
import com.felwal.android.widget.control.getRadioGroupOption
import com.felwal.android.widget.control.inflateRadioGroup
import com.felwal.android.widget.control.putRadioGroupOption

private const val ARG_RADIO_GROUP = "radioGroup"

class RadioSheet : SingleChoiceSheet() {

    private lateinit var radioOption: RadioGroupOption

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            radioOption = getRadioGroupOption(ARG_RADIO_GROUP)
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        setSheetOptions(option, binding)

        inflateRadioGroup(radioOption, binding.fwLl) { selectedIndex ->
            radioOption.checkedIndex = selectedIndex
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        catchClassCast {
            listener?.onSingleChoiceSheetItemSelected(radioOption.checkedIndex, option.tag, option.passValue)
        }
        super.onDismiss(dialog)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: SheetOption,
            radioOption: RadioGroupOption
        ): RadioSheet = RadioSheet().apply {
            arguments = putBaseBundle(option).apply {
                putRadioGroupOption(ARG_RADIO_GROUP, radioOption)
            }
        }
    }
}

fun radioSheet(
    option: SheetOption,
    radioOption: RadioGroupOption
): RadioSheet = RadioSheet.newInstance(option, radioOption)