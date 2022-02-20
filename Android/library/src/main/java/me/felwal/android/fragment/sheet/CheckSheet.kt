package me.felwal.android.fragment.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import me.felwal.android.databinding.FwSheetListBinding
import me.felwal.android.widget.control.CheckListOption
import me.felwal.android.widget.control.SheetOption
import me.felwal.android.widget.control.getCheckListOption
import me.felwal.android.widget.control.inflateCheckList
import me.felwal.android.widget.control.putCheckListOption

private const val ARG_CHECK_LIST = "checkList"

class CheckSheet : MultiChoiceSheet() {

    private lateinit var checkListOption: CheckListOption

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            checkListOption = getCheckListOption(ARG_CHECK_LIST)
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        setSheetOptions(option, binding)

        inflateCheckList(checkListOption, binding.fwLl) { index, isChecked ->
            checkListOption.itemStates[index] = isChecked
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onMultiChoiceSheetItemsSelected(checkListOption.itemStates, option.tag, option.passValue)
        super.onDismiss(dialog)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: SheetOption,
            checkListOption: CheckListOption
        ): CheckSheet = CheckSheet().apply {
            arguments = putBaseBundle(option).apply {
                putCheckListOption(ARG_CHECK_LIST, checkListOption)
            }
        }
    }
}

fun checkSheet(
    option: SheetOption,
    checkListOption: CheckListOption
): CheckSheet = CheckSheet.newInstance(option, checkListOption)