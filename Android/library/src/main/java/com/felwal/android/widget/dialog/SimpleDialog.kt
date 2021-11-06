package com.felwal.android.widget.dialog

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.R
import java.lang.ClassCastException

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
            try {
                listener?.onSimpleDialogItemClick(selectedIndex, dialogTag)
            }
            catch (e: ClassCastException) {
                // listener was not successfully safe-casted to L.
                // all we need to do here is prevent a crash if the listener was not implemented.
                Log.d("Dialog", "Conext was not successfully safe-casted as DialogListener")
            }
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