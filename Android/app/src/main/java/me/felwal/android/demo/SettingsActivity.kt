package me.felwal.android.demo

import android.os.Bundle
import android.widget.LinearLayout
import me.felwal.android.demo.databinding.ActivitySettingsBinding
import me.felwal.android.fragment.app.AbsSettingsActivity
import me.felwal.android.util.snackbar
import me.felwal.android.fragment.dialog.AlertDialog
import me.felwal.android.fragment.dialog.InputDialog
import me.felwal.android.fragment.dialog.MultiChoiceDialog
import me.felwal.android.fragment.dialog.SingleChoiceDialog
import me.felwal.android.fragment.dialog.SliderDialog
import me.felwal.android.util.launchActivity

class SettingsActivity :
    AbsSettingsActivity(dividerMode = DividerMode.AFTER_SECTION, indentEverything = true),
    AlertDialog.DialogListener,
    SingleChoiceDialog.DialogListener,
    MultiChoiceDialog.DialogListener,
    InputDialog.DialogListener,
    SliderDialog.DialogListener {

    // view
    private lateinit var binding: ActivitySettingsBinding
    override val llItemContainer: LinearLayout get() = binding.ll

    private val ic = R.drawable.ic_item
    
    // lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inflateSettingItems()
    }

    // view

    override fun inflateSettingItems() {
        inflateSections(
            ItemSection(
                title = "Non-dialog items",
                BooleanItem(
                    title = "Boolean",
                    value = false,
                    iconRes = ic,
                    onSwitch = {}
                ),
                ActionItem(
                    title = "Action",
                    iconRes = ic,
                    onClick = { snackbar(binding.root, "Action") }
                ),
                LaunchItem(
                    title = "Launch activity",
                    iconRes = ic,
                    activity = SettingsActivity::class.java
                ),
                LinkItem(
                    title = "Open link",
                    iconRes = ic,
                    link = "https://felwal.github.io"
                )
            ),
            ItemSection(
                title = "Dialog items",
                InfoItem(
                    "Info",
                    desc = "Description",
                    msg = "Message",
                    dialogBtnRes = R.string.fw_dialog_btn_ok,
                    iconRes = ic,
                    tag = "tag"
                ),
                ConfirmationItem(
                    "Confirmation",
                    dialogPosBtnRes = R.string.fw_dialog_btn_ok,
                    iconRes = ic,
                    tag = "tag"
                ),
                StringItem(
                    title = "String",
                    value = "Value",
                    hint = "Hint hint",
                    iconRes = ic,
                    tag = "tag"
                ),
                SliderItem(
                    title = "Slider",
                    min = 0f,
                    max = 20f,
                    step = 1f,
                    value = 10f,
                    iconRes = ic,
                    tag = "tag"
                ),
                SingleSelectionItem(
                    title = "Single selection",
                    values = arrayOf("Item", "Item"),
                    selectedIndex = 0,
                    tag = "tag"
                ),
                MultiSelectionItem(
                    title = "Multi selection",
                    values = arrayOf("Item", "Item"),
                    selectedIndices = intArrayOf(0),
                    iconRes = ic,
                    tag = "tag"
                )
            )
        )
    }

    // dialog

    override fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String, passValue: String?) {
        updateDayNight(selectedIndex == 1)
        reflateViews()
    }

    override fun onMultiChoiceDialogItemsSelected(itemStates: BooleanArray, tag: String, passValue: String?) {}

    override fun onInputDialogPositiveClick(input: String, tag: String, passValue: String?) {}

    override fun onSliderDialogPositiveClick(input: Float, tag: String, passValue: String?) {}

    override fun onAlertDialogPositiveClick(tag: String, passValue: String?) {}
}