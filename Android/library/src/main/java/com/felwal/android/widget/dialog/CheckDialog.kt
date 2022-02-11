package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.felwal.android.databinding.FwDialogListBinding
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.control.CheckListOption
import com.felwal.android.widget.control.DialogOption
import com.felwal.android.widget.control.getCheckListOption
import com.felwal.android.widget.control.inflateCheckList
import com.felwal.android.widget.control.putCheckListOption

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