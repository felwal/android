package com.felwal.android.widget.sheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.felwal.android.R
import com.felwal.android.databinding.FwItemDialogListBinding
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.getColorByAttr
import com.felwal.android.util.getDrawableByAttr
import com.felwal.android.util.withFilter

private const val ARG_LABELS = "labels"
private const val ARG_CHECKED_INDEX = "checkedIndex"
private const val ARG_ASCENDING = "ascending"

class SortSheet : BaseSheet<SortSheet.SheetListener>() {

    private lateinit var labels: Array<out String>
    private var checkedIndex = 0
    private var ascending = true

    // BaseSheet

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            checkedIndex = getInt(ARG_CHECKED_INDEX)
            ascending = getBoolean(ARG_ASCENDING)
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)
        val ll = binding.fwLl

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemDialogListBinding.inflate(inflater, ll, false)
            val tv = itemBinding.fwTvLabel

            tv.text = label

            // style selected item
            if (i == checkedIndex) {
                // label
                tv.setTextColor(requireContext().getColorByAttr(R.attr.fw_sortSheetHighlightColor))
                tv.setTypeface(null, Typeface.BOLD)

                // icon
                // for some reason tint doesn't override, so we use filter instead
                val arrow = requireContext().getDrawableByAttr(
                    if (ascending) R.attr.fw_sortSheetAscendingIcon else R.attr.fw_sortSheetDescendingIcon
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
            title: String = "",
            sorter: Sorter<*>,
            tag: String
        ): SortSheet = newInstance(title, sorter.labels.toTypedArray(), sorter.selectedIndex, sorter.ascending, tag)

        @JvmStatic
        fun newInstance(
            title: String = "",
            labels: Array<String>,
            checkedIndex: Int,
            ascending: Boolean,
            tag: String
        ) = SortSheet().apply {
            arguments = putBaseBundle(title, tag).apply {
                putStringArray(ARG_LABELS, labels)
                putInt(ARG_CHECKED_INDEX, checkedIndex)
                putBoolean(ARG_ASCENDING, ascending)
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
        this.selectedIndex = selectedIndex
        this.orderReversed = orderReversed
    }

    fun copy(): Sorter<M> = Sorter(*sortModes).also { it.setSelection(selectedIndex, orderReversed) }
}

data class SortMode<M : Enum<M>>(val label: String, val mode: M, val ascendingByDefault: Boolean)