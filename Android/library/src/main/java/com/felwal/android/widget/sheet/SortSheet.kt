package com.felwal.android.widget.sheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.felwal.android.R
import com.felwal.android.databinding.FwItemSheetListBinding
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.getColorByAttr
import com.felwal.android.util.getDrawableByAttr
import com.felwal.android.util.withFilter
import com.felwal.android.widget.control.SheetOption
import com.felwal.android.widget.control.TriRadioGroupOption
import com.felwal.android.widget.control.getTriRadioGroupOption
import com.felwal.android.widget.control.putTriRadioGroupOption

private const val ARG_TRIRADIO_GROUP = "triRadioGroup"

class SortSheet : BaseSheet<SortSheet.SheetListener>() {

    private lateinit var triRadioOption: TriRadioGroupOption

    // BaseSheet

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
                catchClassCast {
                    listener?.onSortSheetItemClick(i)
                }
                dismiss()
            }
        }

        return binding.root
    }

    //

    interface SheetListener : BaseSheet.SheetListener {
        fun onSortSheetItemClick(checkedIndex: Int)
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
        ) = SortSheet().apply {
            arguments = putBaseBundle(option).apply {
                putTriRadioGroupOption(ARG_TRIRADIO_GROUP, triRadioOption)
            }
        }
    }
}

class Sorter<M : Enum<M>>(vararg val sortModes: SortMode<M>) {

    private val arrows = charArrayOf('↓', '↑')

    var orderReversed = false
        private set
    var selectedIndex = 0
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

    fun copy(): Sorter<M> = Sorter(*sortModes).also { it.setSelection(selectedIndex, orderReversed) }
}

fun Sorter<*>.toTriRadioGroupOption() = TriRadioGroupOption(labels.toTypedArray(), selectedIndex, ascending)

data class SortMode<M : Enum<M>>(val label: String, val mode: M, val ascendingByDefault: Boolean)