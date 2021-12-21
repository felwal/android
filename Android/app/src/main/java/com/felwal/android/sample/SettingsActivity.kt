package com.felwal.android.sample

import android.os.Bundle
import android.widget.LinearLayout
import com.felwal.android.sample.databinding.ActivitySettingsBinding
import com.felwal.android.ui.AbsSettingsActivity
import com.felwal.android.util.snackbar
import com.felwal.android.widget.dialog.DecimalDialog
import com.felwal.android.widget.dialog.SingleChoiceDialog
import com.felwal.android.widget.dialog.TextDialog

class SettingsActivity :
    AbsSettingsActivity(dividerMode = DividerMode.AFTER_SECTION, indentEverything = true),
    SingleChoiceDialog.DialogListener,
    TextDialog.DialogListener,
    DecimalDialog.DialogListener {

    // view
    private lateinit var binding: ActivitySettingsBinding
    override val llItemContainer: LinearLayout get() = binding.ll

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
                    iconRes = R.drawable.fw_ic_check_24,
                    onSwitch = {}
                ),
                ActionItem(
                    title = "Action",
                    iconRes = R.drawable.fw_ic_arrow_up_24,
                    onClick = { snackbar(binding.root, "Action") }
                )
            ),
            ItemSection(
                title = "Dialog items",
                InfoItem(
                    "Info",
                    desc = "Description",
                    msg = "Message",
                    dialogBtnRes = R.string.fw_dialog_btn_ok,
                    iconRes = R.drawable.fw_ic_arrow_down_24,
                    tag = "tag"
                ),
                ConfirmationItem(
                    "Confirmation",
                    dialogPosBtnRes = R.string.fw_dialog_btn_ok,
                    iconRes = R.drawable.fw_ic_check_24,
                    tag = "tag"
                ),
                StringItem(
                    title = "String",
                    value = "Value",
                    hint = "Hint hint",
                    iconRes = R.drawable.fw_ic_arrow_up_24,
                    tag = "tag"
                ),
                SliderItem(
                    title = "Slider",
                    min = 0f,
                    max = 20f,
                    step = 1f,
                    value = 10f,
                    iconRes = R.drawable.fw_ic_arrow_down_24,
                    tag = "tag"
                ),
                SingleSelectionItem(
                    title = "Single selection",
                    values = listOf("Item", "Item"),
                    selectedIndex = 0,
                    tag = "tag"
                ),
                MultiSelectionItem(
                    title = "Multi selection",
                    values = arrayOf("Item", "Item"),
                    selectedIndices = intArrayOf(0),
                    iconRes = R.drawable.fw_ic_check_24,
                    tag = "tag"
                )
            )
        )
    }

    // dialog

    override fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String) {

    }

    override fun onTextDialogPositiveClick(input: String, tag: String) {

    }

    override fun onDecimalDialogPositiveClick(input: Float, tag: String) {

    }
}