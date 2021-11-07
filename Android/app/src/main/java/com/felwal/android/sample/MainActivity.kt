package com.felwal.android.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.felwal.android.lang.Trilean
import com.felwal.android.sample.databinding.ActivityMainBinding
import com.felwal.android.sample.databinding.ItemMainButtonBinding
import com.felwal.android.sample.databinding.ItemMainHeaderBinding
import com.felwal.android.util.getColorAttr
import com.felwal.android.util.popup
import com.felwal.android.util.repeated
import com.felwal.android.util.snackbar
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.NO_RES
import com.felwal.android.widget.dialog.alertDialog
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.chipDialog
import com.felwal.android.widget.dialog.colorDialog
import com.felwal.android.widget.dialog.decimalDialog
import com.felwal.android.widget.dialog.listDialog
import com.felwal.android.widget.dialog.numberDialog
import com.felwal.android.widget.dialog.radioDialog
import com.felwal.android.widget.dialog.textDialog
import com.felwal.android.widget.sheet.SortMode
import com.felwal.android.widget.sheet.SortSheet
import com.felwal.android.widget.sheet.Sorter
import com.felwal.android.widget.sheet.listSheet

class MainActivity : AppCompatActivity(),
    SortSheet.SheetListener
{

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

        header("Message")

        btn("Toast") {
            toast("Message")
        }

        btn("Snackbar") {
            snackbar(it, "Message", true, "Action") {}
        }

        header("Menu")

        btn("Popup") {
            popup(it, R.menu.menu_popup_example)
        }

        header("Dialog")

        btn("Unary") {
            alertDialog("Alert dialog", "Message", negBtnTxtRes = NO_RES, tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Binary") {
            alertDialog("Alert dialog", "Message", tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Ternary") {
            alertDialog("Alert dialog", "Message", neuBtnTxtRes = R.string.app_name, tag = "tag")
                .show(supportFragmentManager)
        }

        btn("List") {
            listDialog(
                "List dialog", "", arrayOf("Item").repeated(12),
                intArrayOf(R.drawable.ic_check_24, R.drawable.ic_arrow_up_24, R.drawable.ic_arrow_down_24).repeated(4),
                tag = "tag"
            )
                .show(supportFragmentManager)
        }

        btn("Radio") {
            radioDialog("Radio dialog", listOf("Item").repeated(20), 0, tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Radio (simple)") {
            radioDialog("Radio dialog", listOf("Item").repeated(20), 0, posBtnTxtRes = null, tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Check") {
            checkDialog("Check dialog", arrayOf("Item").repeated(20), intArrayOf(0), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Color") {
            colorDialog(
                "Color dialog",
                mutableListOf(getColorAttr(android.R.attr.listDivider)).repeated(20).toIntArray(), 0, tag = "tag"
            )
                .show(supportFragmentManager)
        }

        btn("Chip") {
            chipDialog("Chip dialog", arrayOf("Item").repeated(20), intArrayOf(0), tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Number") {
            numberDialog("Number dialog", text = 10, hint = "Hint", tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Decimal") {
            decimalDialog("Decimal dialog", text = 10f, hint = "Hint", tag = "tag")
                .show(supportFragmentManager)
        }

        btn("Text") {
            textDialog("Text dialog", text = "Text", hint = "Hint", tag = "tag")
                .show(supportFragmentManager)
        }

        header("Bottom sheet")

        btn("List") {
            listSheet(
                "List sheet", arrayOf("Item").repeated(3),
                intArrayOf(R.drawable.ic_check_24, R.drawable.ic_arrow_up_24, R.drawable.ic_arrow_down_24),
                "tag"
            )
                .show(supportFragmentManager)
        }

        btn("Sort (with title)") {
            SortSheet.newInstance("Sort by", sorter, "tag")
                .show(supportFragmentManager)
        }

        btn("Sort (without title)") {
            SortSheet.newInstance("", sorter, "tag")
                .show(supportFragmentManager)
        }
    }

    // tool

    private fun btn(label: String, onClick: (View) -> Unit) {
        val btnBinding = ItemMainButtonBinding.inflate(layoutInflater, binding.ll, false)

        btnBinding.tv.text = label
        btnBinding.root.setOnClickListener(onClick)
        binding.ll.addView(btnBinding.root)
    }

    private fun header(title: String) {
        val tvBinding = ItemMainHeaderBinding.inflate(layoutInflater, binding.ll, false)

        tvBinding.tv.text = title
        binding.ll.addView(tvBinding.root)
    }

    // listener

    override fun onSortSheetItemClick(checkedIndex: Int) {
        sorter.select(checkedIndex)
        sorter = sorter.copy()
    }
}