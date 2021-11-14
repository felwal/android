package com.felwal.android.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.felwal.android.R
import com.felwal.android.databinding.FwItemDialogListBinding
import com.felwal.android.util.getDrawableCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

const val NO_RES = -1
const val NULL_INT = -1

private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "message"
private const val ARG_POSITIVE_BUTTON_RES = "positiveButtonText"
private const val ARG_NEGATIVE_BUTTON_RES = "negativeButtonText"
private const val ARG_TAG = "tag"

abstract class BaseDialog<L : BaseDialog.DialogListener> : DialogFragment() {

    protected lateinit var builder: MaterialAlertDialogBuilder
    protected lateinit var inflater: LayoutInflater
    protected var listener: L? = null

    // args
    protected lateinit var title: String
    protected lateinit var message: String
    protected var dialogTag: String = "baseDialog"

    @StringRes protected var posBtnTxtRes: Int = R.string.fw_dialog_btn_ok
    @StringRes protected var negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel

    // DialogFragment

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(requireActivity())
        inflater = requireActivity().layoutInflater

        unpackBundle(unpackBaseBundle())

        return buildDialog()
    }

    override fun onAttach(c: Context) {
        super.onAttach(c)

        @Suppress("UNCHECKED_CAST")
        listener = c as? L
    }

    // bundle

    fun putBaseBundle(
        title: String,
        message: String,
        @StringRes posBtnTxtRes: Int = this.posBtnTxtRes,
        @StringRes negBtnTxtRes: Int = this.negBtnTxtRes,
        tag: String
    ): Bundle = Bundle().apply {
        putString(ARG_TITLE, title)
        putString(ARG_MESSAGE, message)
        putInt(ARG_POSITIVE_BUTTON_RES, posBtnTxtRes)
        putInt(ARG_NEGATIVE_BUTTON_RES, negBtnTxtRes)
        putString(ARG_TAG, tag)
    }

    protected abstract fun unpackBundle(bundle: Bundle?)

    private fun unpackBaseBundle(): Bundle? = arguments?.apply {
        title = getString(ARG_TITLE, "")
        message = getString(ARG_MESSAGE, "")
        posBtnTxtRes = getInt(ARG_POSITIVE_BUTTON_RES, NO_RES)
        negBtnTxtRes = getInt(ARG_NEGATIVE_BUTTON_RES, NO_RES)
        dialogTag = getString(ARG_TAG, dialogTag)
    }

    // build

    protected abstract fun buildDialog(): AlertDialog

    fun show(fm: FragmentManager) {
        if (!isAdded) super.show(fm, dialogTag)
    }

    // tool

    protected fun MaterialAlertDialogBuilder.setTitleIfNonEmpty(title: String) {
        if (title != "") setTitle(title)
    }

    protected fun MaterialAlertDialogBuilder.setMessageIfNonEmpty(message: String) {
        if (message != "") setMessage(message)
    }

    protected fun AlertDialog.Builder.setPositiveButton(
        @StringRes resId: Int,
        listener: (dialog: DialogInterface) -> Unit
    ): AlertDialog.Builder {
        if (resId != NO_RES) setPositiveButton(resId) { dialog, _ -> listener(dialog) }
        return this
    }

    protected fun AlertDialog.Builder.setNeutralButton(
        @StringRes resId: Int,
        listener: (dialog: DialogInterface) -> Unit
    ): AlertDialog.Builder {
        if (resId != NO_RES) setNeutralButton(resId) { dialog, _ -> listener(dialog) }
        return this
    }

    protected fun AlertDialog.Builder.setNegativeButton(
        @StringRes resId: Int,
        listener: (dialog: DialogInterface) -> Unit
    ): AlertDialog.Builder {
        if (resId != NO_RES) setNegativeButton(resId) { dialog, _ -> listener(dialog) }
        return this
    }

    protected fun AlertDialog.Builder.setCancelButton(@StringRes resId: Int): AlertDialog.Builder =
        setNegativeButton(resId) { dialog -> dialog.cancel() }

    protected fun setItems(
        labels: Array<out String>,
        @DrawableRes iconsRes: IntArray?,
        ll: LinearLayout,
        listener: (which: Int) -> Unit
    ) {
        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemDialogListBinding.inflate(inflater, ll, false)

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

    protected fun setDividers(sv: NestedScrollView, vDividerTop: View?, vDividerBottom: View?) {
        // default visibility
        updateDividers(sv, vDividerTop, vDividerBottom)

        // on scroll visibility
        sv.setOnScrollChangeListener { _, _, _, _, _ ->
            updateDividers(sv, vDividerTop, vDividerBottom)
        }

        // on layout size change visibility (includes orientation change)
        sv.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateDividers(sv, vDividerTop, vDividerBottom)
        }
    }

    private fun updateDividers(sv: NestedScrollView, vDividerTop: View?, vDividerBottom: View?) {
        vDividerTop?.isInvisible = !sv.canScrollVertically(-1)
        vDividerBottom?.isInvisible = !sv.canScrollVertically(1)
    }

    protected fun catchClassCast(action: () -> Unit) {
        try {
            action()
        }
        catch (e: ClassCastException) {
            // listener was not successfully safe-casted to L.
            // all we need to do here is prevent a crash if the listener was not implemented.
            Log.w("Dialog", "Conext was not successfully safe-casted as DialogListener")
        }
    }

    //

    interface DialogListener
}

abstract class SingleChoiceDialog : BaseDialog<SingleChoiceDialog.DialogListener>() {
    interface DialogListener : BaseDialog.DialogListener {
        fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String)
    }
}

abstract class MultiChoiceDialog : BaseDialog<MultiChoiceDialog.DialogListener>() {
    interface DialogListener : BaseDialog.DialogListener {
        fun onMultiChoiceDialogItemsSelected(itemStates: BooleanArray, tag: String)
    }
}