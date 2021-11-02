package com.felwal.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.CheckDialog
import com.felwal.android.widget.dialog.ChipDialog
import com.felwal.android.widget.dialog.SimpleDialog
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.chipDialog
import com.felwal.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CheckDialog.DialogListener, ChipDialog.DialogListener, SimpleDialog.DialogListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn("toast") {
            toast("Hello World!")
        }

        btn("check dialog") {
            checkDialog("Just checking in", "", arrayOf("Hej", "Då", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej",
                "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej"
            ), intArrayOf(1), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("chip dialog") {
            chipDialog("Just checking in", "", arrayOf("Hej", "Då", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej",
                "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej"
            ), intArrayOf(1), tag = "tag")
                .show(supportFragmentManager)
        }

    }

    private fun btn(label: String = "", onClick: (View) -> Unit) {
        val btn = Button(this)
        btn.text = label
        btn.setOnClickListener(onClick)
        binding.root.addView(btn)
    }

    //

    override fun onCheckDialogPositiveClick(checkedItems: BooleanArray, tag: String) {
        toast(checkedItems.toString())
    }

    override fun onSimpleDialogItemClick(selectedItem: Int, tag: String) {
        toast(selectedItem.toString())
    }

    override fun onChipDialogPositiveClick(checkedItems: BooleanArray, tag: String) {
        toast(checkedItems.toString())
    }
}