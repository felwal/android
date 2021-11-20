package com.felwal.android.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.felwal.android.lang.Trilean
import com.felwal.android.sample.databinding.ActivityMainBinding
import com.felwal.android.sample.databinding.ItemMainBreakBinding
import com.felwal.android.sample.databinding.ItemMainButtonBinding
import com.felwal.android.sample.databinding.ItemMainHeaderBinding
import com.felwal.android.util.getColorByAttr
import com.felwal.android.util.popup
import com.felwal.android.util.repeated
import com.felwal.android.util.snackbar
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.NO_RES
import com.felwal.android.widget.dialog.SingleChoiceDialog
import com.felwal.android.widget.dialog.alertDialog
import com.felwal.android.widget.dialog.checkDialog
import com.felwal.android.widget.dialog.chipDialog
import com.felwal.android.widget.dialog.colorDialog
import com.felwal.android.widget.dialog.decimalDialog
import com.felwal.android.widget.dialog.listDialog
import com.felwal.android.widget.dialog.numberDialog
import com.felwal.android.widget.dialog.radioDialog
import com.felwal.android.widget.dialog.sliderDialog
import com.felwal.android.widget.dialog.textDialog
import com.felwal.android.widget.sheet.SortMode
import com.felwal.android.widget.sheet.SortSheet
import com.felwal.android.widget.sheet.Sorter
import com.felwal.android.widget.sheet.listSheet

class MainActivity : AppCompatActivity(),
    SortSheet.SheetListener,
    SingleChoiceDialog.DialogListener {

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

        inflateItems()
    }

    private fun inflateItems() {
        // menu

        header("Menu")

        btn("Toast") {
            toast("Message")
        }
        btn("Snackbar") {
            snackbar(it, "Message", true, "Action") {}
        }

        sectionBreak()

        btn("Popup") {
            popup(it, R.menu.menu_popup_example)
        }

        // dialog

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

        sectionBreak()

        btn("List") {
            listDialog(
                "List dialog", "", arrayOf("Item").repeated(12),
                intArrayOf(R.drawable.fw_ic_check_24, R.drawable.fw_ic_arrow_up_24, R.drawable.fw_ic_arrow_down_24).repeated(4),
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
        btn("Color") {
            colorDialog(
                "Color dialog",
                mutableListOf(getColorByAttr(android.R.attr.listDivider)).repeated(20).toIntArray(), 0, tag = "tag"
            )
                .show(supportFragmentManager)
        }

        sectionBreak()

        btn("Check") {
            checkDialog("Check dialog", arrayOf("Item").repeated(20), intArrayOf(0), tag = "tag")
                .show(supportFragmentManager)
        }
        btn("Chip") {
            chipDialog("Chip dialog", arrayOf("Item").repeated(20), intArrayOf(0), tag = "tag")
                .show(supportFragmentManager)
        }

        sectionBreak()

        btn("Slider") {
            sliderDialog("Slider dialog", min = 0f, max = 10f, step = 1f, value = 5f, tag = "tag")
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
            textDialog("Text dialog", "Message", "Text", "Hint", tag = "tag")
                .show(supportFragmentManager)
        }

        // bottom sheet

        header("Bottom sheet")

        btn("List") {
            listSheet(
                "List sheet", arrayOf("Item").repeated(3),
                intArrayOf(R.drawable.fw_ic_check_24, R.drawable.fw_ic_arrow_up_24, R.drawable.fw_ic_arrow_down_24),
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

    // inflate tool

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

    private fun sectionBreak() {
        val breakBinding = ItemMainBreakBinding.inflate(layoutInflater, binding.ll, false)
        binding.ll.addView(breakBinding.root)
    }

    // tool

    private fun updateDayNight(day: Boolean) = AppCompatDelegate.setDefaultNightMode(
        if (day) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
    )

    // listener

    override fun onSortSheetItemClick(checkedIndex: Int) {
        sorter.select(checkedIndex)
        sorter = sorter.copy()

        updateDayNight(true)
    }

    override fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String) {
        updateDayNight(selectedIndex == 1)
    }
}