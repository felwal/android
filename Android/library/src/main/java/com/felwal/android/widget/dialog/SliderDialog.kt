package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogSliderBinding
import com.felwal.android.util.toast

private const val ARG_MIN = "min"
private const val ARG_MAX = "max"
private const val ARG_STEP = "step"
private const val ARG_VALUE = "value"

class SliderDialog : BaseDialog<DecimalDialog.DialogListener>() {

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

            // title & message
            setTitleIfNonEmpty(title)
            setMessageIfNonEmpty(message)

            // buttons
            setPositiveButton(posBtnTxtRes) { _ ->
                try {
                    val input = binding.fwSl.value
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

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            min: Float = 0f,
            max: Float,
            step: Float = 0f,
            value: Float = min,
            @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): SliderDialog = SliderDialog().apply {
            arguments = putBaseBundle(title, message, posBtnTxtRes, negBtnTxtRes, tag).apply {
                putFloat(ARG_MIN, min)
                putFloat(ARG_MAX, max)
                putFloat(ARG_STEP, step)
                putFloat(ARG_VALUE, value.coerceIn(min, max))
            }
        }
    }
}

fun sliderDialog(
    title: String,
    message: String = "",
    min: Float = 0f,
    max: Float,
    step: Float = 0f,
    value: Float = min,
    @StringRes posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): SliderDialog = SliderDialog.newInstance(title, message, min, max, step, value, posBtnTxtRes, negBtnTxtRes, tag)