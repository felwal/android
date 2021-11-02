package com.felwal.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.lang.Trilean
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.CheckDialog
import com.felwal.android.widget.dialog.ChipDialog
import com.felwal.android.widget.dialog.SimpleDialog
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.chipDialog
import com.felwal.android.widget.sheet.SortMode
import com.felwal.android.widget.sheet.SortSheet
import com.felwal.android.widget.sheet.Sorter
import com.felwal.sample.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity(),
    CheckDialog.DialogListener,
    ChipDialog.DialogListener,
    SimpleDialog.DialogListener,
    SortSheet.SheetListener{

    private lateinit var binding: ActivityMainBinding

    private val sorter = Sorter(
        SortMode("Recent", Trilean.NEG, false),
        SortMode("Name", Trilean.NEU, false),
        SortMode("Avg distance", Trilean.POS, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn("Toast") {
            toast("Hello World!")
        }

        btn("Check dialog") {
            checkDialog("Check dialog", "", arrayOf("Hej", "Då", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej",
                "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej"
            ), intArrayOf(1), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Chip dialog") {
            chipDialog("Chip dialog", "", arrayOf("Hej", "Då", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej",
                "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej", "Hej"
            ), intArrayOf(1), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Sorter with title") {
            SortSheet.newInstance("Sort by", sorter, "tag")
                .show(supportFragmentManager)
        }

        btn("Sorter without title") {
            SortSheet.newInstance("", sorter, "tag")
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

    override fun onSortSheetItemClick(checkedIndex: Int) {
        sorter.select(checkedIndex)
    }
}