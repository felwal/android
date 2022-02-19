package me.felwal.android.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import me.felwal.android.demo.databinding.ActivityMainBinding
import me.felwal.android.demo.databinding.ItemMainBreakBinding
import me.felwal.android.demo.databinding.ItemMainButtonBinding
import me.felwal.android.demo.databinding.ItemMainGroupBinding
import me.felwal.android.demo.databinding.ItemMainHeaderBinding
import me.felwal.android.lang.Trilean
import me.felwal.android.util.FLog
import me.felwal.android.util.asIndicesOfTruths
import me.felwal.android.util.contentView
import me.felwal.android.util.getColorByAttr
import me.felwal.android.util.launchActivity
import me.felwal.android.util.multiplyAlphaComponent
import me.felwal.android.util.popup
import me.felwal.android.util.snackbar
import me.felwal.android.util.toIndicesOfTruths
import me.felwal.android.util.toast
import me.felwal.android.widget.control.CheckListOption
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.InputOption
import me.felwal.android.widget.control.ListOption
import me.felwal.android.widget.control.RadioGroupOption
import me.felwal.android.widget.control.SheetOption
import me.felwal.android.widget.dialog.AlertDialog
import me.felwal.android.widget.dialog.MultiChoiceDialog
import me.felwal.android.widget.dialog.NO_RES
import me.felwal.android.widget.dialog.SingleChoiceDialog
import me.felwal.android.widget.dialog.alertDialog
import me.felwal.android.widget.dialog.checkDialog
import me.felwal.android.widget.dialog.chipDialog
import me.felwal.android.widget.dialog.colorDialog
import me.felwal.android.widget.dialog.inputDialog
import me.felwal.android.widget.dialog.listDialog
import me.felwal.android.widget.dialog.radioDialog
import me.felwal.android.widget.dialog.sliderDialog
import me.felwal.android.widget.sheet.MultiChoiceSheet
import me.felwal.android.widget.sheet.SingleChoiceSheet
import me.felwal.android.widget.sheet.SortMode
import me.felwal.android.widget.sheet.SortSheet
import me.felwal.android.widget.sheet.Sorter
import me.felwal.android.widget.sheet.checkSheet
import me.felwal.android.widget.sheet.listSheet
import me.felwal.android.widget.sheet.radioSheet

val log = FLog("Demo")

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
                alertDialog(DialogOption("Alert dialog", "Message", negBtnTxtRes = NO_RES, tag = ""))
                    .show(fm)
            },
            Btn("Binary") {
                alertDialog(diop("Alert dialog", "Message"))
                    .show(fm)
            },
            Btn("Ternary") {
                alertDialog(DialogOption("Alert dialog", "Message", neuBtnTxtRes = R.string.app_name, tag = ""))
                    .show(fm)
            }
        )

        sectionBreak()

        group("List",
            Btn("List") {
                listDialog(diop("List dialog"), ListOption(labels(12), icons(12)))
                    .show(fm)
            },
            Btn("No icons") {
                listDialog(diop("List dialog"), ListOption(labels(12)))
                    .show(fm)
            },
            Btn("No title") {
                listDialog(diop(), ListOption(labels(3), icons(3)))
                    .show(fm)
            },
            Btn("Long no title") {
                listDialog(diop(), ListOption(labels(12), icons(12)))
                    .show(fm)
            },
            Btn("Crash") {
                listDialog(
                    diop("Felwal keeps stopping"),
                    ListOption(arrayOf("Close app"), intArrayOf(R.drawable.fw_ic_cross_24))
                ).show(fm)
            }
        )
        group("Radio",
            Btn("Confirmation") {
                radioDialog(diop("Radio dialog"), RadioGroupOption(labels(9), 0))
                    .show(fm)
            },
            Btn("Simple") {
                radioDialog(
                    DialogOption("Radio dialog", posBtnTxtRes = NO_RES, tag = ""),
                    RadioGroupOption(labels(9), 0)
                )
                    .show(fm)
            },
            Btn("Icons") {
                radioDialog(diop("Radio dialog"), RadioGroupOption(labels(3), 0, icons(3)))
                    .show(fm)
            }
        )
        group("Color",
            Btn("Color") {
                colorDialog(
                    diop("Color dialog"),
                    IntArray(20) { getColorByAttr(R.attr.colorOnSurface).multiplyAlphaComponent(0.15f) }, 0
                ).show(fm)
            },
            Btn("No title") {
                colorDialog(
                    diop(),
                    IntArray(4) { getColorByAttr(R.attr.colorOnSurface).multiplyAlphaComponent(0.15f) }, 0,
                ).show(fm)
            }
        )

        sectionBreak()

        group("Check",
            Btn("Check") {
                checkDialog(diop("Check dialog"), CheckListOption(labels(9), intArrayOf(0)))
                    .show(fm)
            },
            Btn("Icons") {
                checkDialog(
                    diop("Check dialog"),
                    CheckListOption(labels(3), intArrayOf(0), icons(3))
                )
                    .show(fm)
            },
            Btn("Permission") {
                checkDialog(
                    diop("Allow Felwal to make and manage phone calls?"),
                    CheckListOption(arrayOf("Don't ask again"), intArrayOf())
                ).show(fm)
            }
        )
        group("Chip",
            Btn("Chip") {
                chipDialog(diop("Chip dialog"), labels(18), intArrayOf().asIndicesOfTruths(18))
                    .show(fm)
            },
            Btn("No title") {
                chipDialog(diop(), labels(3), intArrayOf().asIndicesOfTruths(3))
                    .show(fm)
            }
        )

        sectionBreak()

        btn("Slider") {
            sliderDialog(diop("Slider dialog"), min = 0f, max = 10f, step = 1f, value = 5f)
                .show(fm)
        }
        btn("Number") {
            inputDialog(diop("Number dialog"), InputOption(10, "Hint"))
                .show(fm)
        }
        btn("Decimal") {
            inputDialog(diop("Decimal dialog"), InputOption(10f, "Hint"))
                .show(fm)
        }
        btn("Text") {
            inputDialog(diop("Text dialog"), InputOption("Text", "Hint"))
                .show(fm)
        }

        // bottom sheet

        header("Bottom sheet")

        group("List",
            Btn("List") {
                listSheet(shop("List sheet"), ListOption(labels(3), icons(3)))
                    .show(fm)
            },
            Btn("No title") {
                listSheet(shop(), ListOption(labels(3), icons(3)))
                    .show(fm)
            },
            Btn("No icons") {
                listSheet(shop("List sheet"), ListOption(labels(3)))
                    .show(fm)
            }
        )
        group("Radio",
            Btn("Radio") {
                radioSheet(shop("Radio sheet"), RadioGroupOption(labels(3), 0))
                    .show(fm)
            },
            Btn("Icons") {
                radioSheet(shop("Radio sheet"), RadioGroupOption(labels(3), 0, icons(3)))
                    .show(fm)
            }
        )
        btn("Sort") {
            SortSheet.newInstance(shop("Sort by"), sorter)
                .show(fm)
        }

        sectionBreak()

        group("Check",
            Btn("Check") {
                checkSheet(shop("Check sheet"), CheckListOption(labels(3), intArrayOf(0)))
                    .show(fm)
            },
            Btn("Icons") {
                checkSheet(shop("Check sheet"), CheckListOption(labels(3), intArrayOf(0), icons(3)))
                    .show(fm)
            },
            Btn("Long") {
                checkSheet(shop("Check sheet"), CheckListOption(labels(18), intArrayOf(0), icons(18)))
                    .show(fm)
            },
            Btn("Long no title") {
                checkSheet(shop(), CheckListOption(labels(18), intArrayOf(0), icons(18)))
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

    private fun diop(title: String = "", message: String = ""): DialogOption =
        DialogOption(title, message, tag = "")

    private fun shop(title: String = "", message: String = ""): SheetOption =
        SheetOption(title, message, tag = "")

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