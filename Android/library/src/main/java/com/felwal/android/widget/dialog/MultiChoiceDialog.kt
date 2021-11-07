package com.felwal.android.widget.dialog

abstract class MultiChoiceDialog : BaseDialog<MultiChoiceDialog.DialogListener>() {

    interface DialogListener : BaseDialog.DialogListener {
        fun onMultiChoiceDialogItemsSelected(itemStates: BooleanArray, tag: String)
    }
}