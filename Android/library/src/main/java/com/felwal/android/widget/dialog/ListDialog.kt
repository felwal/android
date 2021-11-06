package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.databinding.DialogListBinding
import com.felwal.android.util.orEmpty

private const val ARG_LABELS = "labels"
private const val ARG_ICONS = "icons"

class ListDialog : BaseDialog<ListDialog.DialogListener>() {

    // args
    private lateinit var labels: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = DialogListBinding.inflate(inflater)
        setView(binding.root)

        // title & message
        setTitleIfNonEmpty(title)

        // widget
        setDividers(binding.sv, binding.vDividerTop, null)

        // items
        setItems(labels, iconsRes, binding.ll) { selectedIndex ->
            catchClassCast {
                listener?.onListDialogItemClick(selectedIndex, dialogTag)
            }
        }

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onListDialogItemClick(selectedIndex: Int, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            labels: Array<String>,
            @DrawableRes icons: IntArray?,
            tag: String
        ): ListDialog = ListDialog().apply {
            arguments = putBaseBundle(title, "", NO_RES, NO_RES, tag).apply {
                putStringArray(ARG_LABELS, labels)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun listDialog(
    title: String,
    labels: Array<String>,
    @DrawableRes icons: IntArray?,
    tag: String
): ListDialog = ListDialog.newInstance(title, labels, icons, tag)