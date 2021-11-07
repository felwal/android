package com.felwal.android.widget.dialog

abstract class SingleChoiceDialog : BaseDialog<SingleChoiceDialog.DialogListener>() {

    interface DialogListener : BaseDialog.DialogListener {
        fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String)
    }
}