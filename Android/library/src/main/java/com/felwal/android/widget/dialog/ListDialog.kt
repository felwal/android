package com.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.felwal.android.databinding.FwDialogListBinding
import com.felwal.android.widget.control.DialogOption
import com.felwal.android.widget.control.ListOption
import com.felwal.android.widget.control.getListOption
import com.felwal.android.widget.control.inflateList
import com.felwal.android.widget.control.putListOption

private const val ARG_LIST = "list"

class ListDialog : SingleChoiceDialog() {

    // args
    private lateinit var listOption: ListOption

    // BaseDialog

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            listOption = getListOption(ARG_LIST)
        }
    }

    override fun buildDialog(): AlertDialog = builder.run {
        val binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)
        setDividers(binding.fwSv, binding.fwVDividerTop, binding.fwVDividerBottom)

        setDialogOptions(option)

        inflateList(listOption, binding.fwLl) { index ->
            listener?.onSingleChoiceDialogItemSelected(index, option.tag, option.passValue)
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
            listOption: ListOption
        ): ListDialog = ListDialog().apply {
            arguments = putBaseBundle(option).apply {
                putListOption(ARG_LIST, listOption)
            }
        }
    }
}

fun listDialog(
    option: DialogOption,
    listOption: ListOption
): ListDialog = ListDialog.newInstance(option, listOption)