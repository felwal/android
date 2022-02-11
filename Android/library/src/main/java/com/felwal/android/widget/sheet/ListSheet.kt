package com.felwal.android.widget.sheet

import android.os.Bundle
import android.view.View
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.control.ListOption
import com.felwal.android.widget.control.SheetOption
import com.felwal.android.widget.control.getListOption
import com.felwal.android.widget.control.inflateList
import com.felwal.android.widget.control.putListOption

private const val ARG_LIST = "list"

class ListSheet : SingleChoiceSheet() {

    private lateinit var listOption: ListOption

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            listOption = getListOption(ARG_LIST)
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        setSheetOptions(option, binding)

        inflateList(listOption, binding.fwLl) { selectedIndex ->
            listener?.onSingleChoiceSheetItemSelected(selectedIndex, option.tag, option.passValue)
        }

        return binding.root
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: SheetOption,
            listOption: ListOption
        ) = ListSheet().apply {
            arguments = putBaseBundle(option).apply {
                putListOption(ARG_LIST, listOption)
            }
        }
    }
}

fun listSheet(
    option: SheetOption,
    listOption: ListOption
): ListSheet = ListSheet.newInstance(option, listOption)