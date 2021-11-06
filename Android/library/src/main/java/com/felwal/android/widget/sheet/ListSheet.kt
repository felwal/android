package com.felwal.android.widget.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import com.felwal.android.databinding.SheetListBinding
import com.felwal.android.util.orEmpty
import java.lang.ClassCastException

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

        setTitle(title, binding)

        setItems(items, iconsRes, binding.ll) { selectedIndex ->
            try {
                listener?.onListSheetItemClick(selectedIndex)
            }
            catch (e: ClassCastException) {
                // listener was not successfully safe-casted to L.
                // all we need to do here is prevent a crash if the listener was not implemented.
                Log.d("Dialog", "Conext was not successfully safe-casted as SheetListener")
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