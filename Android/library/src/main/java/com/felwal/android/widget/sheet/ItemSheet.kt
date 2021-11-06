package com.felwal.android.widget.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import com.felwal.android.R
import com.felwal.android.databinding.ItemSheetItemBinding
import com.felwal.android.databinding.SheetItemBinding
import com.felwal.android.util.backgroundTint
import com.felwal.android.util.getColorAttr
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.util.getDrawableCompatFilter
import com.felwal.android.util.orEmpty
import com.felwal.android.widget.dialog.NO_RES
import java.lang.ClassCastException

private const val ARG_ITEMS = "items"
private const val ARG_ICONS = "icons"

class ItemSheet : BaseSheet<ItemSheet.SheetListener>() {

    private lateinit var items: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            items = getStringArray(ARG_ITEMS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildSheet(): View {
        val binding = SheetItemBinding.inflate(inflater)
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
            val itemBinding = ItemSheetItemBinding.inflate(inflater, ll, false)

            itemBinding.tvLabel.text = label

            // icon
            val iconRes = iconsRes[i]
            if (iconRes != NO_RES) {
                val icon = requireContext().getDrawableCompat(iconRes)
                itemBinding.ivIcon.setImageDrawable(icon)
            }

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                try {
                    listener?.onItemSheetItemClick(i)
                }
                catch (e: ClassCastException) {
                    // listener was not successfully safe-casted to L.
                    // all we need to do here is prevent a crash if the listener was not implemented.
                    Log.d("Dialog", "Conext was not successfully safe-casted as SheetListener")
                }
                dismiss()
            }
        }

        return binding.root
    }

    //

    interface SheetListener : BaseSheet.SheetListener {
        fun onItemSheetItemClick(index: Int)
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String = "",
            items: Array<String>,
            @DrawableRes icons: IntArray,
            tag: String
        ) = ItemSheet().apply {
            arguments = putBaseBundle(title, tag).apply {
                putStringArray(ARG_ITEMS, items)
                putIntArray(ARG_ICONS, icons)
            }
        }
    }
}

fun itemSheet(
    title: String = "",
    items: Array<String>,
    @DrawableRes icons: IntArray,
    tag: String
): ItemSheet = ItemSheet.newInstance(title, items, icons, tag)