package me.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import me.felwal.android.databinding.FwDialogListBinding
import me.felwal.android.widget.control.CheckListOption
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.getCheckListOption
import me.felwal.android.widget.control.inflateCheckList
import me.felwal.android.widget.control.putCheckListOption

private const val ARG_CHECK_LIST = "icons"

class CheckDialog : MultiChoiceDialog() {

    // args
    private lateinit var checkListOption: CheckListOption

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            checkListOption = getCheckListOption(ARG_CHECK_LIST)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option) {
            listener?.onMultiChoiceDialogItemsSelected(checkListOption.itemStates, option.tag, option.passValue)
        }

        inflateCheckList(checkListOption, binding.fwLl) { index, isChecked ->
            checkListOption.itemStates[index] = isChecked
        }

        show().apply {
            fixScrollingDialogCustomPanelPadding()
        }
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: DialogOption,
            checkListOption: CheckListOption
        ): CheckDialog = CheckDialog().apply {
            arguments = putBaseBundle(option).apply {
                putCheckListOption(ARG_CHECK_LIST, checkListOption)
            }
        }
    }
}

fun checkDialog(
    option: DialogOption,
    checkListOption: CheckListOption
): CheckDialog = CheckDialog.newInstance(option, checkListOption)