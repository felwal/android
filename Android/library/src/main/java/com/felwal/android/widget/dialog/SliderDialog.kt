package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogSliderBinding
import com.felwal.android.util.toast
import com.felwal.android.widget.control.DialogOption

private const val ARG_MIN = "min"
private const val ARG_MAX = "max"
private const val ARG_STEP = "step"
private const val ARG_VALUE = "value"

class SliderDialog : BaseDialog<SliderDialog.DialogListener>() {

    // args
    private var min = 0f
    private var max = 0f
    private var step = 0f
    private var value = 0f

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            min = getFloat(ARG_MIN, 0f)
            max = getFloat(ARG_MAX, 0f)
            step = getFloat(ARG_STEP, 0f)
            value = getFloat(ARG_VALUE, 0f)
        }
    }

    override fun buildDialog(): AlertDialog {
        val binding = FwDialogSliderBinding.inflate(inflater)

        // widget
        binding.fwSl.also {
            it.value = value
            it.valueFrom = min
            it.valueTo = max
            it.stepSize = step
        }

        return builder.run {
            setView(binding.root)

            setDialogOptions(option) {
                try {
                    val input = binding.fwSl.value
                    listener?.onSliderDialogPositiveClick(input, option.tag, option.passValue)
                }
                catch (e: NumberFormatException) {
                    activity?.toast(R.string.fw_toast_e_input)
                }
            }

            show()
        }
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onSliderDialogPositiveClick(input: Float, tag: String, passValue: String?)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            min: Float = 0f,
            max: Float,
            step: Float = 0f,
            value: Float = min,
        ): SliderDialog = SliderDialog().apply {
            arguments = putBaseBundle(option).apply {
                putFloat(ARG_MIN, min)
                putFloat(ARG_MAX, max)
                putFloat(ARG_STEP, step)
                putFloat(ARG_VALUE, value.coerceIn(min, max))
            }
        }
    }
}

fun sliderDialog(
    option: DialogOption,
    min: Float = 0f,
    max: Float,
    step: Float = 0f,
    value: Float = min,
): SliderDialog = SliderDialog.newInstance(option, min, max, step, value)