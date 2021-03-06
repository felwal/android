package me.felwal.android.fragment.sheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import me.felwal.android.R
import me.felwal.android.databinding.FwItemSheetListBinding
import me.felwal.android.databinding.FwSheetListBinding
import me.felwal.android.util.getColorByAttr
import me.felwal.android.util.getDrawableByAttr
import me.felwal.android.util.withFilter
import me.felwal.android.widget.control.SheetOption
import me.felwal.android.widget.control.TriRadioGroupOption
import me.felwal.android.widget.control.getTriRadioGroupOption
import me.felwal.android.widget.control.putTriRadioGroupOption

private const val ARG_TRIRADIO_GROUP = "triRadioGroup"

class SortSheet : BaseSheet<SortSheet.SheetListener>() {

    // args
    private lateinit var triRadioOption: TriRadioGroupOption

    //

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            triRadioOption = getTriRadioGroupOption(ARG_TRIRADIO_GROUP)
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)
        val ll = binding.fwLl

        setSheetOptions(option, binding)

        // items
        for ((i, label) in triRadioOption.labels.withIndex()) {
            val itemBinding = FwItemSheetListBinding.inflate(inflater, ll, false)
            val tv = itemBinding.fwTvLabel

            tv.text = label

            // style selected item
            if (i == triRadioOption.checkedIndex) {
                // label
                tv.setTextColor(requireContext().getColorByAttr(R.attr.fw_sortSheetHighlightColor))
                tv.setTypeface(null, Typeface.BOLD)

                // icon
                // for some reason tint doesn't override, so we use filter instead
                val arrow = requireContext().getDrawableByAttr(
                    if (triRadioOption.ascending) R.attr.fw_sortSheetAscendingIcon
                    else R.attr.fw_sortSheetDescendingIcon
                )?.withFilter(requireContext().getColorByAttr(R.attr.fw_sortSheetHighlightColor))
                itemBinding.fwIvIcon.setImageDrawable(arrow)
            }

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                listener?.onSortSheetItemClick(i)
                dismiss()
            }
        }

        return binding.root
    }

    //

    interface SheetListener : BaseSheet.SheetListener {
        fun onSortSheetItemClick(checkedIndex: Int)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putTriRadioGroupOption(ARG_TRIRADIO_GROUP, triRadioOption)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            option: SheetOption,
            sorter: Sorter<*>
        ): SortSheet = newInstance(option, sorter.toTriRadioGroupOption())

        @JvmStatic
        fun newInstance(
            option: SheetOption,
            triRadioOption: TriRadioGroupOption
        ): SortSheet = SortSheet().apply {
            arguments = putBaseBundle(option).apply {
                putTriRadioGroupOption(ARG_TRIRADIO_GROUP, triRadioOption)
            }
        }
    }
}

class Sorter<M : Enum<M>>(vararg val sortModes: SortMode<M>) {

    private val arrows: CharArray = charArrayOf('???', '???')

    var orderReversed: Boolean = false
        private set
    var selectedIndex: Int = 0
        private set

    val ascending: Boolean
        get() = if (orderReversed) !sortMode.ascendingByDefault else sortMode.ascendingByDefault

    val sortMode: SortMode<M>
        get() = sortModes[selectedIndex]

    val mode: M
        get() = sortMode.mode

    val labels: List<String>
        get() = sortModes.map { it.label }

    val layoutString: String
        get() = sortMode.label + " " + arrows[if (ascending) 1 else 0]

    //

    fun select(index: Int) {
        if (index >= sortModes.size) return
        if (selectedIndex == index) orderReversed = !orderReversed
        else {
            selectedIndex = index
            orderReversed = false
        }
    }

    fun setSelection(selectedIndex: Int, orderReversed: Boolean) {
        this.selectedIndex = selectedIndex.coerceIn(0, sortModes.size - 1)
        this.orderReversed = orderReversed
    }

    fun copy(): Sorter<M> =
        Sorter(*sortModes).also { it.setSelection(selectedIndex, orderReversed) }
}

fun Sorter<*>.toTriRadioGroupOption(): TriRadioGroupOption =
    TriRadioGroupOption(labels.toTypedArray(), selectedIndex, ascending)

data class SortMode<M : Enum<M>>(val label: String, val mode: M, val ascendingByDefault: Boolean)