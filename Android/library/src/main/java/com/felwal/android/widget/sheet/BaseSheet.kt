package com.felwal.android.widget.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

    //

    interface SheetListener
}