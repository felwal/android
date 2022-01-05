package com.felwal.android.sample

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.felwal.android.lang.Trilean
import com.felwal.android.sample.databinding.ActivityMainBinding
import com.felwal.android.sample.databinding.ItemMainBreakBinding
import com.felwal.android.sample.databinding.ItemMainButtonBinding
import com.felwal.android.sample.databinding.ItemMainGroupBinding
import com.felwal.android.sample.databinding.ItemMainHeaderBinding
import com.felwal.android.util.getColorByAttr
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.util.launchActivity
import com.felwal.android.util.multiplyAlphaComponent
import com.felwal.android.util.popup
import com.felwal.android.util.repeated
import com.felwal.android.util.snackbar
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.AlertDialog
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

class MainActivity :
    AppCompatActivity(),
    AlertDialog.DialogListener,
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
        binding.fam.onSetContentView()

        initFam()
        inflateItems()
    }

    private fun initFam() {
        // fam
        binding.fam.apply {
            addItem("Item", R.drawable.fw_ic_check_24) {
                snackbar("item clicked")
                closeMenu()
            }
            addItem("Item", R.drawable.fw_ic_arrow_up_24) {
                snackbar("item clicked")
                closeMenu()
            }
            addItem("Item", R.drawable.fw_ic_arrow_down_24) {
                snackbar("item clicked")
                closeMenu()
            }
        }
        binding.fam.setAutoUpdateVisibilityOnScroll(binding.sv)
    }

    private fun inflateItems() {
        btn("Settings") {
            launchActivity<SettingsActivity>()
        }
        group("Theme",
            Btn("Day") { updateDayNight(true) },
            Btn("Night") { updateDayNight(false) }
        )

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

        group("Alert",
            Btn("Unary") {
                alertDialog("Alert dialog", "Message", negBtnTxtRes = NO_RES, tag = "tag")
                    .show(supportFragmentManager)
            },
            Btn("Binary") {
                alertDialog("Alert dialog", "Message", tag = "tag")
                    .show(supportFragmentManager)
            },
            Btn("Ternary") {
                alertDialog(
                    "Alert dialog", "Long ${"long ".repeat(200)}message",
                    neuBtnTxtRes = R.string.app_name, tag = "tag"
                ).show(supportFragmentManager)
            }
        )

        sectionBreak()

        btn("List") {
            listDialog(
                "List dialog", "", arrayOf("Item").repeated(12),
                intArrayOf(R.drawable.fw_ic_check_24, R.drawable.fw_ic_arrow_up_24, R.drawable.fw_ic_arrow_down_24).repeated(4),
                tag = "tag"
            )
                .show(supportFragmentManager)
        }
        group("Radio",
            Btn("Confirmation") {
                radioDialog("Radio dialog", listOf("Item").repeated(20), 0, tag = "tag")
                    .show(supportFragmentManager)
            },
            Btn("Simple") {
                radioDialog("Radio dialog", listOf("Item").repeated(20), 0, posBtnTxtRes = null, tag = "tag")
                    .show(supportFragmentManager)
            }
        )
        btn("Color") {
            colorDialog(
                "Color dialog",
                mutableListOf(
                    getColorByAttr(R.attr.colorOnSurface).multiplyAlphaComponent(0.15f)
                ).repeated(20).toIntArray(),
                0, tag = "tag"
            ).show(supportFragmentManager)
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
            ).show(supportFragmentManager)
        }
        group("Sort",
            Btn("With title") {
                SortSheet.newInstance("Sort by", sorter, "tag")
                    .show(supportFragmentManager)
            },
            Btn("Without title") {
                SortSheet.newInstance("", sorter, "tag")
                    .show(supportFragmentManager)
            }
        )
    }

    // inflate tool

    private inner class Btn(val label: String, val onClick: (View) -> Unit) {
        fun inflate(root: ViewGroup = binding.ll) = btn(label, root, onClick)
    }

    private fun btn(label: String, onClick: (View) -> Unit) = btn(label, binding.ll, onClick)

    private fun btn(label: String, root: ViewGroup, onClick: (View) -> Unit) {
        val btnBinding = ItemMainButtonBinding.inflate(layoutInflater, root, false)

        btnBinding.tv.text = label
        btnBinding.root.setOnClickListener(onClick)
        root.addView(btnBinding.root)
    }

    private fun group(label: String, vararg btns: Btn) {
        val groupBinding = ItemMainGroupBinding.inflate(layoutInflater, binding.ll, false)

        groupBinding.tv.text = label

        for (btn in btns) btn.inflate(groupBinding.root)

        binding.ll.addView(groupBinding.root)
    }

    private fun header(title: String) {
        val tvBinding = ItemMainHeaderBinding.inflate(layoutInflater, binding.ll, false)

        tvBinding.tv.text = title
        //tvBinding.iv.setImageDrawable(getDrawableCompatWithTint(R.drawable.fw_ic_check_24, R.attr.colorControlNormal))

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

    override fun onAlertDialogPositiveClick(passValue: String?, tag: String) {
    }
}