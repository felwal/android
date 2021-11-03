package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R

private const val ARG_ITEMS = "items"

class SimpleDialog : BaseDialog<SimpleDialog.DialogListener>() {

    // args
    private lateinit var items: Array<out String>

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        setTitle(title)

        setItems(items) { _, selectedIndex: Int ->
            listener?.onSimpleDialogItemClick(selectedIndex, dialogTag)
        }

        show()
    }

    //

    interface DialogListener : BaseDialog.DialogListener {
        fun onSimpleDialogItemClick(selectedItem: Int, tag: String)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            items: Array<String>,
            tag: String
        ): SimpleDialog = SimpleDialog().apply {
            arguments = putBaseBundle(title, "", NO_RES, NO_RES, tag).apply {
                putStringArray(ARG_ITEMS, items)
            }
        }
    }
}

fun simpleDialog(
    title: String,
    items: Array<String>,
    tag: String
): SimpleDialog = SimpleDialog.newInstance(title, items, tag)