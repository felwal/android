package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.databinding.DialogListBinding
import com.felwal.android.util.orEmpty

private const val ARG_LABELS = "labels"
private const val ARG_ICONS = "icons"

class ListDialog : SingleChoiceDialog() {

    // args
    private lateinit var labels: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }

        if (labels.size != iconsRes.size) {
            throw IndexOutOfBoundsException("labels and iconsRes must have equal size")
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = DialogListBinding.inflate(inflater)
        setView(binding.root)

        // title & message
        setTitleIfNonEmpty(title)
        setMessageIfNonEmpty(message)

        // widget
        setDividers(binding.sv, binding.vDividerTop, null)

        // items
        setItems(labels, iconsRes, binding.ll) { index ->
            catchClassCast {
                listener?.onSingleChoiceDialogItemSelected(index, dialogTag)
            }
        }

        show()
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            message: String = "",
            labels: Array<String>,
            @DrawableRes icons: IntArray?,
            tag: String
        ): ListDialog = ListDialog().apply {
            arguments = putBaseBundle(title, message, NO_RES, NO_RES, tag).apply {
                putStringArray(ARG_LABELS, labels)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun listDialog(
    title: String,
    message: String = "",
    labels: Array<String>,
    @DrawableRes icons: IntArray?,
    tag: String
): ListDialog = ListDialog.newInstance(title, message, labels, icons, tag)