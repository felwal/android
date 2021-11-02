package com.felwal.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.CheckDialog
import com.felwal.android.widget.dialog.SimpleDialog
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.simpleDialog

class MainActivity : AppCompatActivity(), CheckDialog.DialogListener, SimpleDialog.DialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("Hello World!")

        simpleDialog("Just checking in", arrayOf("Hej", "Då"), tag = "tag")
            .show(supportFragmentManager)
    }

    override fun onCheckDialogPositiveClick(checkedItems: BooleanArray, tag: String) {
        toast(checkedItems.toString())
    }

    override fun onSimpleDialogItemClick(selectedItem: Int, tag: String) {
        toast(selectedItem.toString())
    }
}