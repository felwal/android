package com.felwal.android.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.felwal.android.R
import com.felwal.android.databinding.ItemDialogListBinding
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

    @StringRes protected var posBtnTxtRes: Int = R.string.dialog_btn_ok
    @StringRes protected var negBtnTxtRes: Int = R.string.dialog_btn_cancel

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
        posBtnTxtRes = getInt(ARG_POSITIVE_BUTTON_RES, posBtnTxtRes)
        negBtnTxtRes = getInt(ARG_NEGATIVE_BUTTON_RES, negBtnTxtRes)
        dialogTag = getString(ARG_TAG, dialogTag)
    }

    // build

    private fun styleDialog(dialog: AlertDialog): AlertDialog = dialog.apply {
        setTitleTextAppearanceAppCompat(resources, R.style.TextAppearance_Felwal_Dialog_Title)
        setMessageTextAppearance(R.style.TextAppearance_Felwal_Dialog_Body)
    }

    protected abstract fun buildDialog(): AlertDialog

    fun show(fm: FragmentManager) {
        if (!isAdded) super.show(fm, dialogTag)
    }

    // tool

    protected fun setItems(
        labels: Array<out String>,
        @DrawableRes iconsRes: IntArray?,
        ll: LinearLayout,
        listener: (which: Int) -> Unit
    ) {
        for ((i, label) in labels.withIndex()) {
            val itemBinding = ItemDialogListBinding.inflate(inflater, ll, false)

            // label
            itemBinding.tvLabel.text = label

            // icon
            if (iconsRes != null && iconsRes.isNotEmpty()) {
                val iconRes = iconsRes[i]
                if (iconRes != NO_RES) {
                    val icon = requireContext().getDrawableCompat(iconRes)
                    itemBinding.ivIcon.setImageDrawable(icon)
                }
            }
            else {
                itemBinding.ivIcon.isGone = true
            }

            ll.addView(itemBinding.root)

            itemBinding.root.setOnClickListener {
                listener(i)
                dismiss()
            }
        }
    }

    protected fun setDividers(sv: NestedScrollView, vDividerTop: View?, vDividerBottom: View?) {
        // TODO: show on open, hide on height/item update

        // default visibility
        vDividerTop?.isInvisible = !sv.canScrollVertically(-1)
        vDividerBottom?.isInvisible = !sv.canScrollVertically(1)

        // on scroll visibility
        sv.setOnScrollChangeListener { _, _, _, _, _ ->
            vDividerTop?.isInvisible = !sv.canScrollVertically(-1)
            vDividerBottom?.isInvisible = !sv.canScrollVertically(1)
        }
    }

    //

    interface DialogListener
}

fun AlertDialog.Builder.setCancelButton(@StringRes resId: Int): AlertDialog.Builder =
    setNegativeButton(resId) { dialog, _ -> dialog.cancel() }

fun AlertDialog.setTitleTextAppearanceAppCompat(res: Resources, @StyleRes resId: Int) =
    setTitleTextAppearance(res, resId, context.packageName)

fun AlertDialog.setTitleTextAppearanceAndroid(res: Resources, @StyleRes resId: Int) =
    setTitleTextAppearance(res, resId, "android")

fun AlertDialog.setTitleTextAppearance(res: Resources, @StyleRes resId: Int, defPackage: String) =
    res.getIdentifier("alertTitle", "id", defPackage)
        .takeIf { it > 0 }
        ?.let { titleId ->
            findViewById<TextView>(titleId)?.setTextAppearance(resId)
        }

fun AlertDialog.setMessageTextAppearance(@StyleRes resId: Int) =
    findViewById<TextView>(android.R.id.message)?.setTextAppearance(resId)