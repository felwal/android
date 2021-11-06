package com.felwal.android.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.lang.Trilean
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.chipDialog
import com.felwal.android.widget.sheet.SortMode
import com.felwal.android.widget.sheet.SortSheet
import com.felwal.android.widget.sheet.Sorter
import com.felwal.android.sample.databinding.ActivityMainBinding
import com.felwal.android.util.getColorAttr
import com.felwal.android.util.repeated
import com.felwal.android.widget.dialog.colorDialog
import com.felwal.android.widget.dialog.simpleDialog
import com.felwal.android.widget.dialog.unaryDialog

class MainActivity: AppCompatActivity(), SortSheet.SheetListener {

    private lateinit var binding: ActivityMainBinding

    private var sorter = Sorter(
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

        btn("Simple dialog") {
            simpleDialog("Simple dialog", arrayOf("Item").repeated(10), tag = "")
                .show(supportFragmentManager)
        }

        btn("Unary dialog") {
            unaryDialog("Unary dialog", tag = "")
                .show(supportFragmentManager)
        }

        btn("Check dialog") {
            checkDialog("Check dialog", "", arrayOf("Hej").repeated(20), intArrayOf(1), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Color dialog") {
            colorDialog("Color dialog", "",
                mutableListOf(getColorAttr(android.R.attr.colorSecondary)).repeated(32).toIntArray(), 0, tag = "tag"
            )
                .show(supportFragmentManager)
        }

        btn("Chip dialog") {
            chipDialog("Chip dialog", "", arrayOf("Hej").repeated(20), intArrayOf(1), tag = "tag")
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

    override fun onSortSheetItemClick(checkedIndex: Int) {
        sorter.select(checkedIndex)
        sorter = sorter.copy()
    }
}