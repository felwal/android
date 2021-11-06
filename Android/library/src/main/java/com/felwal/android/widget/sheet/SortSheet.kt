package com.felwal.android.widget.sheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import com.felwal.android.R
import com.felwal.android.databinding.ItemSheetSortBinding
import com.felwal.android.databinding.SheetSortBinding
import com.felwal.android.util.getColorAttr
import com.felwal.android.util.getDrawableAttr

private const val ARG_ITEMS = "items"
private const val ARG_CHECKED_ITEM = "checkedItem"
private const val ARG_ASCENDING = "ascending"

class SortSheet : BaseSheet<SortSheet.SheetListener>() {

    private lateinit var items: Array<out String>
    private var checkedItem = 0
    private var ascending = true

    // BaseSheet

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
            checkedItem = getInt(ARG_CHECKED_ITEM)
            ascending = getBoolean(ARG_ASCENDING)
        }
    }

    override fun buildSheet(): View {
        val binding = SheetSortBinding.inflate(inflater)
        val ll = binding.ll

        // title
        if (title == "") {
            binding.tvTitle.isGone = true
            binding.div.isGone = true
        }
        else {
            binding.tvTitle.text = title
        }

        // items
        for ((i, label) in items.withIndex()) {
            val itemBinding = ItemSheetSortBinding.inflate(inflater, ll, false)
            val tv = itemBinding.tvLabel

            tv.text = label

            // style selected
            if (i == checkedItem) {
                // label
                tv.setTextColor(requireContext().getColorAttr(R.attr.sortSheetHighlightColor))
                tv.setTypeface(null, Typeface.BOLD)

                // icon
                val arrow = requireContext().getDrawableAttr(
                    if (ascending) R.attr.sortSheetUpArrow else R.attr.sortSheetDownArrow,
                    R.attr.sortSheetHighlightColor
                )
                itemBinding.iv.setImageDrawable(arrow)
            }

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                listener.onSortSheetItemClick(i)
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
            items: Array<String>,
            checkedItem: Int,
            ascending: Boolean,
            tag: String
        ) = SortSheet().apply {
            arguments = putBaseBundle(title, tag).apply {
                putStringArray(ARG_ITEMS, items)
                putInt(ARG_CHECKED_ITEM, checkedItem)
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