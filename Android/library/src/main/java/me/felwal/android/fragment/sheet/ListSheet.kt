package me.felwal.android.fragment.sheet

import android.os.Bundle
import android.view.View
import me.felwal.android.databinding.FwSheetListBinding
import me.felwal.android.widget.control.ListOption
import me.felwal.android.widget.control.SheetOption
import me.felwal.android.widget.control.getListOption
import me.felwal.android.widget.control.inflateList
import me.felwal.android.widget.control.putListOption

private const val ARG_LIST = "list"

class ListSheet : SingleChoiceSheet() {

    // args
    private lateinit var listOption: ListOption

    //

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putListOption(ARG_LIST, listOption)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: SheetOption,
            listOption: ListOption
        ): ListSheet = ListSheet().apply {
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