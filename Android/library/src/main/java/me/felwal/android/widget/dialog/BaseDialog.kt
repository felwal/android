package me.felwal.android.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.felwal.android.R
import me.felwal.android.databinding.FwItemDialogCheckBinding
import me.felwal.android.databinding.FwItemDialogListBinding
import me.felwal.android.databinding.FwItemDialogRadioBinding
import me.felwal.android.util.canScrollDown
import me.felwal.android.util.canScrollUp
import me.felwal.android.util.getDimension
import me.felwal.android.util.getDrawableCompat
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.getDialogOption
import me.felwal.android.widget.control.putDialogOption

const val NO_RES = -1
const val NULL_INT = -1

private const val ARG_DIALOG = "dialog"

abstract class BaseDialog<L : BaseDialog.DialogListener> : DialogFragment() {

    protected lateinit var builder: MaterialAlertDialogBuilder
    protected lateinit var inflater: LayoutInflater
    protected var listener: L? = null

    // args
    protected lateinit var option: DialogOption

    protected open val hasButtons
        get() = option.posBtnTxtRes != NO_RES || option.negBtnTxtRes != NO_RES || option.neuBtnTxtRes != NO_RES

    private val hasTitle get() = option.title.isNotEmpty()

    // DialogFragment

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(requireActivity())
        inflater = requireActivity().layoutInflater

        return buildDialog().also { styleDialog(it) }
    }

    override fun onAttach(c: Context) {
        super.onAttach(c)

        @Suppress("UNCHECKED_CAST")
        listener = c as L
    }

    // bundle

    fun putBaseBundle(option: DialogOption): Bundle = Bundle().apply {
        putDialogOption(ARG_DIALOG, option)
    }

    protected abstract fun unpackBundle(bundle: Bundle?)

    private fun unpackBaseBundle(): Bundle? = arguments?.apply {
        option = getDialogOption(ARG_DIALOG)
    }

    // build

    protected abstract fun buildDialog(): AlertDialog

    private fun styleDialog(dialog: AlertDialog) {
        // the content panel, which contains the message tv, has a minHeight of 48dp by default,
        // which gives nice spacing between it and the dialog buttons.
        // however, when the dialog contains a custom view,
        // it creates a big ugly gap between the message and the custom panel.
        if (dialog.customPanel?.isVisible == true) {
            dialog.contentPanel?.minimumHeight = 0
        }
    }

    fun show(fm: FragmentManager) {
        unpackBundle(unpackBaseBundle())
        if (!isAdded) super.show(fm, option.tag)
    }

    // set text

    fun MaterialAlertDialogBuilder.setDialogOptions(
        option: DialogOption,
        neuBtnListener: (() -> Unit)? = null,
        posBtnListener: (() -> Unit)? = null
    ) {
        // text
        setTitleIfNonEmpty(option.title)
        setMessageIfNonEmpty(option.message)

        // button
        setPositiveButton(option.posBtnTxtRes) { _ ->
            posBtnListener?.invoke()
        }
        setNeutralButton(option.neuBtnTxtRes) { _ ->
            neuBtnListener?.invoke()
        }
        setCancelButton(option.negBtnTxtRes)
    }

    protected fun MaterialAlertDialogBuilder.setTitleIfNonEmpty(title: String) {
        if (title != "") setTitle(title)
    }

    protected fun MaterialAlertDialogBuilder.setMessageIfNonEmpty(message: String) {
        if (message != "") setMessage(message)
    }

    // set button

    fun AlertDialog.Builder.setPositiveButton(
        @StringRes resId: Int,
        listener: (dialog: DialogInterface) -> Unit
    ): AlertDialog.Builder {
        if (resId != NO_RES) setPositiveButton(resId) { dialog, _ ->
            listener(dialog)
            dismiss()
        }
        return this
    }

    protected fun AlertDialog.Builder.setNeutralButton(
        @StringRes resId: Int,
        listener: (dialog: DialogInterface) -> Unit
    ): AlertDialog.Builder {
        if (resId != NO_RES) setNeutralButton(resId) { dialog, _ ->
            listener(dialog)
            dismiss()
        }
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
            val itemBinding = FwItemDialogListBinding.inflate(layoutInflater, ll, false)

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
        val itemBindings = mutableListOf<FwItemDialogRadioBinding>()

        for ((i, label) in labels.withIndex()) {
            val itemBinding = FwItemDialogRadioBinding.inflate(layoutInflater, ll, false)

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
                }

                listener(i)
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
            val itemBinding = FwItemDialogCheckBinding.inflate(layoutInflater, ll, false)

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

    /**
     * Adds bottom padding to the custom panel if the there are no buttons,
     * and moves top padding if there is no title.
     *
     * Use this to give the ScrollView symmetric and non-clipping vertical padding.
     */
    protected fun AlertDialog.fixScrollingDialogCustomPanelPadding() {
        // remove padding from root and add to ll (to allow clipToPadding to take effect)
        if (!hasTitle) {
            // remove padding from root top
            customPanel?.findViewById<ConstraintLayout>(R.id.cl_root)
                ?.updatePadding(top = 0)

            // add padding to ll top
            customPanel?.findViewById<LinearLayout>(R.id.fw_ll)
                ?.updatePadding(top = context.getDimension(R.dimen.fw_spacing_tiny).toInt())
        }

        if (!hasButtons) {
            // add padding to ll bottom
            customPanel?.findViewById<LinearLayout>(R.id.fw_ll)
                ?.updatePadding(bottom = context.getDimension(R.dimen.fw_spacing_tiny).toInt())
        }
    }

    protected fun setDividers(vList: View, vDividerTop: View?, vDividerBottom: View?) {
        // default visibility
        updateDividers(vList, vDividerTop, vDividerBottom)
        if (!hasTitle) vDividerTop?.isInvisible = true
        if (!hasButtons) vDividerBottom?.isInvisible = true

        // on scroll visibility
        vList.setOnScrollChangeListener { _, _, _, _, _ ->
            updateDividers(vList, vDividerTop, vDividerBottom)
        }

        // on layout size change visibility (includes orientation change)
        vList.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateDividers(vList, vDividerTop, vDividerBottom)
        }
    }

    private fun updateDividers(vList: View, vDividerTop: View?, vDividerBottom: View?) {
        if (hasTitle) vDividerTop?.isInvisible = !vList.canScrollUp()
        if (hasButtons) vDividerBottom?.isInvisible = !vList.canScrollDown()
    }

    //

    interface DialogListener
}

//

abstract class SingleChoiceDialog : BaseDialog<SingleChoiceDialog.DialogListener>() {
    interface DialogListener : BaseDialog.DialogListener {
        fun onSingleChoiceDialogItemSelected(selectedIndex: Int, tag: String, passValue: String?)
    }
}

abstract class MultiChoiceDialog : BaseDialog<MultiChoiceDialog.DialogListener>() {
    interface DialogListener : BaseDialog.DialogListener {
        fun onMultiChoiceDialogItemsSelected(itemStates: BooleanArray, tag: String, passValue: String?)
    }
}

//

val AlertDialog.titleTextView: TextView?
    get() = context.resources.getIdentifier("alertTitle", "id", context.packageName)
        .takeIf { it > 0 }
        ?.let { titleId -> findViewById(titleId) }

val AlertDialog.titleTextViewAndroid: TextView?
    get() = context.resources.getIdentifier("alertTitle", "id", "android")
        .takeIf { it > 0 }
        ?.let { titleId -> findViewById(titleId) }

val AlertDialog.messageTextView: TextView? get() = findViewById(android.R.id.message)

val AlertDialog.customPanel: FrameLayout? get() = findViewById(R.id.customPanel)

val AlertDialog.contentPanel: FrameLayout? get() = findViewById(R.id.contentPanel)