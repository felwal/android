package com.felwal.android.widget.sheet

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.orEmpty

private const val ARG_LABELS = "labels"
private const val ARG_ICONS = "icons"

class ListSheet : BaseSheet<ListSheet.SheetListener>() {

    private lateinit var labels: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }

        if (labels.size != iconsRes.size) {
            throw IndexOutOfBoundsException("labels and iconsRes must have equal size")
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        setItems(labels, iconsRes, binding.fwLl) { selectedIndex ->
            catchClassCast {
                listener?.onListSheetItemClick(selectedIndex)
            }
        }

        return binding.root
    }

    //

    interface SheetListener : BaseSheet.SheetListener {
        fun onListSheetItemClick(selectedIndex: Int)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String = "",
            labels: Array<String>,
            @DrawableRes icons: IntArray?,
            tag: String
        ) = ListSheet().apply {
            arguments = putBaseBundle(title, tag).apply {
                putStringArray(ARG_LABELS, labels)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun listSheet(
    title: String = "",
    labels: Array<String>,
    @DrawableRes icons: IntArray?,
    tag: String
): ListSheet = ListSheet.newInstance(title, labels, icons, tag)