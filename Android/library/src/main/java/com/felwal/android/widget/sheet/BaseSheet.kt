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
import androidx.fragment.app.FragmentManager
import com.felwal.android.databinding.FwItemSheetListBinding
import com.felwal.android.databinding.FwSheetListBinding
import com.felwal.android.util.getDrawableCompat
import com.felwal.android.widget.dialog.NO_RES
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException

private const val ARG_TITLE = "title"
private const val ARG_TAG = "tag"

abstract class BaseSheet<L : BaseSheet.SheetListener> : BottomSheetDialogFragment() {

    protected lateinit var inflater: LayoutInflater
    protected var listener: L? = null

    // arguments
    protected var title: String = ""
    protected var sheetTag: String = "baseSheet"

    // DialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = requireActivity().layoutInflater

        unpackBundle(unpackBaseBundle())
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

    fun putBaseBundle(title: String, tag: String): Bundle = Bundle().apply {
        putString(ARG_TITLE, title)
        putString(ARG_TAG, tag)
    }

    protected abstract fun unpackBundle(bundle: Bundle?)

    private fun unpackBaseBundle(): Bundle? = arguments?.apply {
        title = getString(ARG_TITLE, "")
        sheetTag = getString(ARG_TAG, sheetTag)
    }

    // build

    protected abstract fun buildSheet(): View

    fun show(fm: FragmentManager) {
        if (!isAdded) super.show(fm, sheetTag)
    }

    // tool

    protected fun setTitleIfNonEmpty(title: String, binding: FwSheetListBinding) {
        if (title == "") {
            binding.fwTvTitle.isGone = true
            binding.fwVDivider.isGone = true
        }
        else {
            binding.fwTvTitle.text = title
        }
    }

    protected fun setItems(
        labels: Array<out String>,
        @DrawableRes iconsRes: IntArray?,
        ll: LinearLayout,
        listener: (which: Int) -> Unit
    ) {
        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemSheetListBinding.inflate(inflater, ll, false)

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

    protected fun catchClassCast(action: () -> Unit) {
        try {
            action()
        }
        catch (e: ClassCastException) {
            // listener was not successfully safe-casted to L.
            // all we need to do here is prevent a crash if the listener was not implemented.
            Log.d("Sheet", "Conext was not successfully safe-casted as SheetListener")
        }
    }

    //

    interface SheetListener
}