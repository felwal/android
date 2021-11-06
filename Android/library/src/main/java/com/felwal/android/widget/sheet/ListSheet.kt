package com.felwal.android.widget.sheet

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.felwal.android.databinding.SheetListBinding
import com.felwal.android.util.orEmpty

private const val ARG_ITEMS = "items"
private const val ARG_ICONS = "icons"

class ListSheet : BaseSheet<ListSheet.SheetListener>() {

    private lateinit var items: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildSheet(): View {
        val binding = SheetListBinding.inflate(inflater)

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        setItems(items, iconsRes, binding.ll) { selectedIndex ->
            catchClassCast {
                listener?.onListSheetItemClick(selectedIndex)
            }
        }

        return binding.root
    }

    //

    interface SheetListener : BaseSheet.SheetListener {
        fun onListSheetItemClick(index: Int)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String = "",
            items: Array<String>,
            @DrawableRes icons: IntArray?,
            tag: String
        ) = ListSheet().apply {
            arguments = putBaseBundle(title, tag).apply {
                putStringArray(ARG_ITEMS, items)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun listSheet(
    title: String = "",
    items: Array<String>,
    @DrawableRes icons: IntArray?,
    tag: String
): ListSheet = ListSheet.newInstance(title, items, icons, tag)