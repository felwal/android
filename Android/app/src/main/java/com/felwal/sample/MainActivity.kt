package com.felwal.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.CheckDialog
import com.felwal.android.widget.dialog.checkDialog

class MainActivity : AppCompatActivity(), CheckDialog.DialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("Hello World!")

        checkDialog("Just checking in", "wow", arrayOf("Hej", "Då"), intArrayOf(1), tag = "tag")
            .show(supportFragmentManager)
    }

    override fun onCheckDialogPositiveClick(checkedItems: BooleanArray, tag: String) {}
}