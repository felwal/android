package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import com.felwal.android.databinding.FwDialogListBinding
import com.felwal.android.util.orEmpty

private const val ARG_LABELS = "labels"
private const val ARG_CHECKED_INDEX = "checkedIndex"
private const val ARG_ICONS = "icons"

class RadioDialog : SingleChoiceDialog() {

    // args
    private lateinit var labels: Array<out String>
    private var checkedIndex = 0
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            checkedIndex = getInt(ARG_CHECKED_INDEX, 0).coerceIn(0, labels.size)
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)

        // title
        setTitleIfNonEmpty(title)

        // widget
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        // items
        setSingleChoiceItems(labels, checkedIndex, iconsRes, binding.fwLl) { index ->
            // there is no positive button; make the dialog simple, i.e. dismiss on item click
            if (posBtnTxtRes == NO_RES) {
                catchClassCast {
                    listener?.onSingleChoiceDialogItemSelected(index, dialogTag)
                }
                dialog?.cancel()
            }
            else checkedIndex = index
        }

        // buttons
        setPositiveButton(posBtnTxtRes) { _ ->
            catchClassCast {
                listener?.onSingleChoiceDialogItemSelected(checkedIndex, dialogTag)
            }
        }
        setCancelButton(negBtnTxtRes)

        show()
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            labels: List<String>,
            checkedIndex: Int,
            @DrawableRes icons: IntArray? = null,
            @StringRes posBtnTxtRes: Int? = R.string.fw_dialog_btn_ok,
            @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
            tag: String
        ): RadioDialog = RadioDialog().apply {
            arguments = putBaseBundle(title, "", posBtnTxtRes ?: NO_RES, negBtnTxtRes = negBtnTxtRes, tag = tag).apply {
                putStringArray(ARG_LABELS, labels.toTypedArray())
                putInt(ARG_CHECKED_INDEX, checkedIndex)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun radioDialog(
    title: String,
    labels: List<String>,
    checkedIndex: Int,
    @DrawableRes icons: IntArray? = null,
    @StringRes posBtnTxtRes: Int? = R.string.fw_dialog_btn_ok,
    @StringRes negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    tag: String
): RadioDialog = RadioDialog.newInstance(title, labels, checkedIndex, icons, posBtnTxtRes, negBtnTxtRes, tag)