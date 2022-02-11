package com.felwal.android.widget.sheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentManager
import com.felwal.android.databinding.FwItemSheetCheckBinding
import com.felwal.android.databinding.FwItemSheetListBinding
import com.felwal.android.databinding.FwItemSheetRadioBinding
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.widget.control.DialogOption
import com.felwal.android.widget.control.SheetOption
import com.felwal.android.widget.control.getSheetOption
import com.felwal.android.widget.control.putSheetOption
import com.felwal.android.widget.dialog.NO_RES
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ClassCastException

private const val ARG_SHEET = "title"

abstract class BaseSheet<L : BaseSheet.SheetListener> : BottomSheetDialogFragment() {

    protected lateinit var inflater: LayoutInflater
    protected var listener: L? = null

    // arguments
    protected lateinit var option: SheetOption

    // DialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = requireActivity().layoutInflater
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return buildSheet()
    }

    override fun onAttach(c: Context) {
        super.onAttach(c)

        @Suppress("UNCHECKED_CAST")
        listener = c as? L
    }

    // bundle

    fun putBaseBundle(option: SheetOption): Bundle = Bundle().apply {
        putSheetOption(ARG_SHEET, option)
    }

    protected abstract fun unpackBundle(bundle: Bundle?)

    private fun unpackBaseBundle(): Bundle? = arguments?.apply {
        option = getSheetOption(ARG_SHEET)
    }

    // build

    protected abstract fun buildSheet(): View

    fun show(fm: FragmentManager) {
        unpackBundle(unpackBaseBundle())
        if (!isAdded) super.show(fm, option.tag)
    }

    // text

    fun setSheetOptions(option: SheetOption, binding: FwSheetListBinding) {
        setTitleIfNonEmpty(option.title, binding)
        // TODO: message
    }

    // set items

    fun setItems(
        labels: Array<out String>,
        @DrawableRes iconsRes: IntArray? = null,
        ll: LinearLayout,
        listener: (which: Int) -> Unit
    ) {
        if (iconsRes != null && iconsRes.isNotEmpty() && iconsRes.size != labels.size) {
            throw IndexOutOfBoundsException("`iconsRes` must be null, empty or have equal size as `labels`.")
        }

        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemSheetListBinding.inflate(layoutInflater, ll, false)

            // label
            itemBinding.fwTvLabel.text = label

            // icon
            if (iconsRes != null && iconsRes.isNotEmpty()) {
                val iconRes = iconsRes[i]
                if (iconRes != NO_RES) {
                    val icon = requireContext().getDrawableCompat(iconRes)
                    itemBinding.fwIvIcon.setImageDrawable(icon)
                }
            }
            else {
                itemBinding.fwIvIcon.isGone = true
            }

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                listener(i)
                dismiss()
            }
        }
    }

    fun setSingleChoiceItems(
        labels: Array<out String>,
        checkedIndex: Int,
        @DrawableRes iconsRes: IntArray? = null,
        ll: LinearLayout,
        listener: (which: Int) -> Unit
    ) {
        if (iconsRes != null && iconsRes.isNotEmpty() && iconsRes.size != labels.size) {
            throw IndexOutOfBoundsException("`iconsRes` must be null, empty or have equal size as `labels`.")
        }

        // use this for deselecting items
        val itemBindings = mutableListOf<FwItemSheetRadioBinding>()

        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemSheetRadioBinding.inflate(layoutInflater, ll, false)

            // label
            itemBinding.fwTvLabel.text = label

            // icon
            if (iconsRes != null && iconsRes.isNotEmpty()) {
                val iconRes = iconsRes[i]
                if (iconRes != NO_RES) {
                    val icon = requireContext().getDrawableCompat(iconRes)
                    itemBinding.fwIvIcon.setImageDrawable(icon)
                }
                itemBinding.fwRbStart.isGone = true
            }
            else {
                // invisible to keep label correctly positioned
                itemBinding.fwIvIcon.isInvisible = true
                itemBinding.fwRbEnd.isGone = true
            }

            // selection
            if (i == checkedIndex) {
                itemBinding.fwRbStart.isChecked = true
                itemBinding.fwRbEnd.isChecked = true
            }

            ll.addView(itemBinding.root)
            itemBindings.add(itemBinding)

            itemBinding.root.setOnClickListener {
                // dont reselect the same item twice
                if (!itemBinding.fwRbStart.isChecked) {
                    // deselect all other
                    itemBindings.forEach {
                        it.fwRbStart.isChecked = false
                        it.fwRbEnd.isChecked = false
                    }

                    // select this
                    itemBinding.fwRbStart.isChecked = true
                    itemBinding.fwRbEnd.isChecked = true

                    listener(i)
                }
            }
        }
    }

    fun setMultiChoiceItems(
        labels: Array<out String>,
        itemStates: BooleanArray,
        @DrawableRes iconsRes: IntArray? = null,
        ll: LinearLayout,
        listener: (which: Int, isChecked: Boolean) -> Unit
    ) {
        if (iconsRes != null && iconsRes.isNotEmpty() && iconsRes.size != labels.size) {
            throw IndexOutOfBoundsException("`iconsRes` must be null, empty or have equal size as `labels`.")
        }
        if (labels.size != itemStates.size) {
            throw IndexOutOfBoundsException("`labels` and `itemStates` must have equal size")
        }

        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemSheetCheckBinding.inflate(layoutInflater, ll, false)

            // label
            itemBinding.fwTvLabel.text = label

            // icon
            if (iconsRes != null && iconsRes.isNotEmpty()) {
                val iconRes = iconsRes[i]
                if (iconRes != NO_RES) {
                    val icon = requireContext().getDrawableCompat(iconRes)
                    itemBinding.fwIvIcon.setImageDrawable(icon)
                }
                itemBinding.fwCbStart.isGone = true
            }
            else {
                // invisible to keep label correctly positioned
                itemBinding.fwIvIcon.isInvisible = true
                itemBinding.fwCbEnd.isGone = true
            }

            // selection
            itemBinding.fwCbStart.isChecked = itemStates[i]
            itemBinding.fwCbEnd.isChecked = itemStates[i]

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                // toggle
                itemBinding.fwCbStart.toggle()
                itemBinding.fwCbEnd.toggle()

                listener(i, itemBinding.fwCbStart.isChecked)
            }
        }
    }

    // more tools

    protected fun setTitleIfNonEmpty(title: String, binding: FwSheetListBinding) {
        if (title == "") {
            binding.fwTvTitle.isGone = true
            binding.fwVDivider.isGone = true
        }
        else {
            binding.fwTvTitle.text = title
        }
    }

    protected fun catchClassCast(action: () -> Unit) {
        try {
            action()
        }
        catch (e: ClassCastException) {
            // listener was not successfully safe-casted to L.
            // all we need to do here is prevent a crash if the listener was not implemented.
            Log.w("Sheet", "Conext was not successfully safe-casted as SheetListener")
        }
    }

    //

    interface SheetListener
}

//

abstract class SingleChoiceSheet : BaseSheet<SingleChoiceSheet.SheetListener>() {
    interface SheetListener : BaseSheet.SheetListener {
        fun onSingleChoiceSheetItemSelected(selectedIndex: Int, tag: String, passValue: String?)
    }
}

abstract class MultiChoiceSheet : BaseSheet<MultiChoiceSheet.SheetListener>() {
    interface SheetListener : BaseSheet.SheetListener {
        fun onMultiChoiceSheetItemsSelected(itemStates: BooleanArray, tag: String, passValue: String?)
    }
}