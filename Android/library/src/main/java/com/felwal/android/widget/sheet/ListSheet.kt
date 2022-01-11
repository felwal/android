package com.felwal.android.widget.sheet

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.orEmpty

private const val ARG_LABELS = "labels"
private const val ARG_ICONS = "icons"

class ListSheet : SingleChoiceSheet() {

    private lateinit var labels: Array<out String>
    @DrawableRes private var iconsRes: IntArray = intArrayOf()

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            labels = getStringArray(ARG_LABELS).orEmpty()
            iconsRes = getIntArray(ARG_ICONS).orEmpty()
        }
    }

    override fun buildSheet(): View {
        val binding = FwSheetListBinding.inflate(inflater)

        // title
        setTitleIfNonEmpty(title, binding)

        // items
        setItems(labels, iconsRes, binding.fwLl) { selectedIndex ->
            catchClassCast {
                listener?.onSingleChoiceSheetItemSelected(selectedIndex, sheetTag, passValue)
            }
        }

        return binding.root
    }

    //

    companion object {
        @JvmStatic
        fun newInstance(
            title: String = "",
            labels: Array<String>,
            @DrawableRes icons: IntArray? = null,
            tag: String,
            passValue: String? = null
        ) = ListSheet().apply {
            arguments = putBaseBundle(title, tag, passValue).apply {
                putStringArray(ARG_LABELS, labels)
                putIntArray(ARG_ICONS, icons.orEmpty())
            }
        }
    }
}

fun listSheet(
    title: String = "",
    labels: Array<String>,
    @DrawableRes icons: IntArray? = null,
    tag: String,
    passValue: String? = null
): ListSheet = ListSheet.newInstance(title, labels, icons, tag, passValue)