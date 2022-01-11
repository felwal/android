package com.felwal.android.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.felwal.android.lang.Trilean
import com.felwal.android.demo.databinding.ActivityMainBinding
import com.felwal.android.demo.databinding.ItemMainBreakBinding
import com.felwal.android.demo.databinding.ItemMainButtonBinding
import com.felwal.android.demo.databinding.ItemMainGroupBinding
import com.felwal.android.demo.databinding.ItemMainHeaderBinding
import com.felwal.android.util.contentView
import com.felwal.android.util.getColorByAttr
import com.felwal.android.util.launchActivity
import com.felwal.android.util.multiplyAlphaComponent
import com.felwal.android.util.popup
import com.felwal.android.util.snackbar
import com.felwal.android.util.toIndicesOfTruths
import com.felwal.android.util.toast
import com.felwal.android.widget.dialog.AlertDialog
import com.felwal.android.widget.dialog.MultiChoiceDialog
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
import com.felwal.android.widget.sheet.MultiChoiceSheet
import com.felwal.android.widget.sheet.SingleChoiceSheet
import com.felwal.android.widget.sheet.SortMode
import com.felwal.android.widget.sheet.SortSheet
import com.felwal.android.widget.sheet.Sorter
import com.felwal.android.widget.sheet.checkSheet
import com.felwal.android.widget.sheet.listSheet
import com.felwal.android.widget.sheet.radioSheet

class MainActivity :
    AppCompatActivity(),
    AlertDialog.DialogListener,
    SortSheet.SheetListener,
    SingleChoiceDialog.DialogListener,
    MultiChoiceDialog.DialogListener,
    SingleChoiceSheet.SheetListener,
    MultiChoiceSheet.SheetListener {

    private lateinit var binding: ActivityMainBinding

    private val fm get() = supportFragmentManager

    private var sorter = Sorter(
        SortMode("Recent", Trilean.NEG, false),
        SortMode("Name", Trilean.NEU, false),
        SortMode("Avg distance", Trilean.POS, true)
    )

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fam.onSetContentView()

        initFam()
        inflateItems()
    }

    //

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
                    .show(fm)
            },
            Btn("Binary") {
                alertDialog("Alert dialog", "Message", tag = "tag")
                    .show(fm)
            },
            Btn("Ternary") {
                alertDialog(
                    "Alert dialog", "Message",
                    neuBtnTxtRes = R.string.app_name, tag = "tag"
                ).show(fm)
            }
        )

        sectionBreak()

        group("List",
            Btn("List") {
                listDialog("List dialog", "", labels(12), icons(12), tag = "tag")
                    .show(fm)
            },
            Btn("No icons") {
                listDialog("List dialog", "", labels(12), tag = "tag")
                    .show(fm)
            },
            Btn("No title") {
                listDialog("", "", labels(3), icons(3), tag = "tag")
                    .show(fm)
            },
            Btn("Long no title") {
                listDialog("", "", labels(12), icons(12), tag = "tag")
                    .show(fm)
            },
            Btn("Crash") {
                listDialog(
                    "Felwal keeps stopping", "",
                    arrayOf("Close app"), intArrayOf(R.drawable.fw_ic_cross_24), tag = "tag"
                ).show(fm)
            }
        )
        group("Radio",
            Btn("Confirmation") {
                radioDialog("Radio dialog", labels(9), 0, tag = "tag")
                    .show(fm)
            },
            Btn("Simple") {
                radioDialog("Radio dialog", labels(9), 0, posBtnTxtRes = null, tag = "tag")
                    .show(fm)
            },
            Btn("Icons") {
                radioDialog("Radio dialog", labels(3), 0, icons(3), tag = "tag")
                    .show(fm)
            }
        )
        group("Color",
            Btn("Color") {
                colorDialog(
                    "Color dialog",
                    IntArray(20) { getColorByAttr(R.attr.colorOnSurface).multiplyAlphaComponent(0.15f) }, 0,
                    tag = "tag"
                ).show(fm)
            },
            Btn("No title") {
                colorDialog(
                    "",
                    IntArray(4) { getColorByAttr(R.attr.colorOnSurface).multiplyAlphaComponent(0.15f) },0,
                    tag = "tag"
                ).show(fm)
            }
        )

        sectionBreak()

        group("Check",
            Btn("Check") {
                checkDialog("Check dialog", labels(9), intArrayOf(0), tag = "tag")
                    .show(fm)
            },
            Btn("Icons") {
                checkDialog("Check dialog", labels(3), intArrayOf(0), icons(3), tag = "tag")
                    .show(fm)
            },
            Btn("Permission") {
                checkDialog(
                    "Allow Felwal to make and manage phone calls?",
                    arrayOf("Don't ask again"), intArrayOf(), tag = "tag"
                ).show(fm)
            }
        )
        group("Chip",
            Btn("Chip") {
                chipDialog("Chip dialog", labels(18), intArrayOf(), tag = "tag")
                    .show(fm)
            },
            Btn("No title") {
                chipDialog("", labels(3), intArrayOf(), tag = "tag")
                    .show(fm)
            }
        )

        sectionBreak()

        btn("Slider") {
            sliderDialog("Slider dialog", min = 0f, max = 10f, step = 1f, value = 5f, tag = "tag")
                .show(fm)
        }
        btn("Number") {
            numberDialog("Number dialog", text = 10, hint = "Hint", tag = "tag")
                .show(fm)
        }
        btn("Decimal") {
            decimalDialog("Decimal dialog", text = 10f, hint = "Hint", tag = "tag")
                .show(fm)
        }
        btn("Text") {
            textDialog("Text dialog", "Message", "Text", "Hint", tag = "tag")
                .show(fm)
        }

        // bottom sheet

        header("Bottom sheet")

        group("List",
            Btn("List") {
                listSheet("List sheet", labels(3), icons(3), "tag")
                    .show(fm)
            },
            Btn("No title") {
                listSheet("", labels(3), icons(3), "tag")
                    .show(fm)
            },
            Btn("No icons") {
                listSheet("List sheet", labels(3), tag = "tag")
                    .show(fm)
            }
        )
        group("Radio",
            Btn("Radio") {
                radioSheet("Radio sheet", labels(3), 0, tag = "tag")
                    .show(fm)
            },
            Btn("Icons") {
                radioSheet("Radio sheet", labels(3), 0, icons(3), tag = "tag")
                    .show(fm)
            }
        )
        btn("Sort") {
            SortSheet.newInstance("Sort by", sorter, "tag")
                .show(fm)
        }

        sectionBreak()

        group("Check",
            Btn("Check") {
                checkSheet("Check sheet", labels(3), intArrayOf(0), tag = "tag")
                    .show(fm)
            },
            Btn("Icons") {
                checkSheet("Check sheet", labels(3), intArrayOf(0), icons(3), tag = "tag")
                    .show(fm)
            },
            Btn("Long") {
                checkSheet("List sheet", labels(18), intArrayOf(0), icons(18), tag = "tag")
                    .show(fm)
            },
            Btn("Long no title") {
                checkSheet("", labels(18), intArrayOf(0), icons(18), tag = "tag")
                    .show(fm)
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

    private fun labels(count: Int) = Array(count) { "Item" }

    private fun icons(count: Int): IntArray = IntArray(count) { i ->
        when (i % 3) {
            0 -> R.drawable.fw_ic_check_24
            1 -> R.drawable.fw_ic_arrow_up_24
            else -> R.drawable.fw_ic_arrow_down_24
        }
    }

    // dialog listener

    override fun onAlertDialogPositiveClick(tag: String, passValue: String?) {
    }

    override fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String, passValue: String?) {
        //updateDayNight(selectedIndex == 1)
        contentView?.snackbar(selectedIndex.toString())
    }

    override fun onMultiChoiceDialogItemsSelected(itemStates: BooleanArray, tag: String, passValue: String?) {
        contentView?.snackbar(itemStates.toIndicesOfTruths().contentToString())
    }

    // sheet listener

    override fun onSortSheetItemClick(checkedIndex: Int) {
        sorter.select(checkedIndex)
        sorter = sorter.copy()
    }

    override fun onSingleChoiceSheetItemSelected(selectedIndex: Int, tag: String, passValue: String?) {
        contentView?.snackbar(selectedIndex.toString())
    }

    override fun onMultiChoiceSheetItemsSelected(itemStates: BooleanArray, tag: String, passValue: String?) {
        contentView?.snackbar(itemStates.toIndicesOfTruths().contentToString())
    }
}