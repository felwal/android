package me.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.chip.Chip
import me.felwal.android.databinding.FwComponentChipBinding
import me.felwal.android.databinding.FwDialogChipBinding
import me.felwal.android.util.orEmpty
import me.felwal.android.widget.control.DialogOption

private const val ARG_LABELS = "labels"
private const val ARG_ITEM_STATES = "itemStates"

class ChipDialog : MultiChoiceDialog() {

    // args
    private lateinit var labels: Array<out String>
    private lateinit var itemStates: BooleanArray

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            itemStates = getBooleanArray(ARG_ITEM_STATES).orEmpty().copyOf()
        }

        if (labels.size != itemStates.size) {
            throw IndexOutOfBoundsException("labels and itemStates must have equal size")
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = FwDialogChipBinding.inflate(inflater)
        setView(binding.root)
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option) {
            listener?.onMultiChoiceDialogItemsSelected(itemStates, option.tag, option.passValue)
        }

        // items
        val chipGroup = binding.fwCg
        for (i in labels.indices) {
            val chip: Chip = FwComponentChipBinding.inflate(inflater, chipGroup, false).root
            chipGroup.addView(chip)
            chip.text = labels[i]
            chip.isChecked = itemStates[i]

            chip.setOnCheckedChangeListener { _, isChecked ->
                itemStates[i] = isChecked
            }
        }

        show()
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            labels: Array<String>,
            itemStates: BooleanArray,
        ): ChipDialog = ChipDialog().apply {
            arguments = putBaseBundle(option).apply {
                putStringArray(ARG_LABELS, labels)
                putBooleanArray(ARG_ITEM_STATES, itemStates)
            }
        }
    }
}

fun chipDialog(
    option: DialogOption,
    labels: Array<String>,
    itemStates: BooleanArray,
): ChipDialog = ChipDialog.newInstance(option, labels, itemStates)