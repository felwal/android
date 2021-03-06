package me.felwal.android.fragment.app

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import me.felwal.android.R
import me.felwal.android.databinding.FwItemSettingsHeaderBinding
import me.felwal.android.databinding.FwItemSettingsSwitchBinding
import me.felwal.android.databinding.FwItemSettingsTextBinding
import me.felwal.android.fragment.dialog.BaseDialog
import me.felwal.android.fragment.dialog.NO_LONG_TEXT
import me.felwal.android.fragment.dialog.NO_RES
import me.felwal.android.fragment.dialog.alertDialog
import me.felwal.android.fragment.dialog.checkDialog
import me.felwal.android.fragment.dialog.inputDialog
import me.felwal.android.fragment.dialog.radioDialog
import me.felwal.android.fragment.dialog.sliderDialog
import me.felwal.android.util.getDimension
import me.felwal.android.util.getDrawableCompat
import me.felwal.android.util.getResIdByAttr
import me.felwal.android.util.hideOrRemove
import me.felwal.android.util.launchActivity
import me.felwal.android.util.openLink
import me.felwal.android.util.setItemRipple
import me.felwal.android.util.setTextRemoveIfEmpty
import me.felwal.android.widget.control.CheckListOption
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.InputOption
import me.felwal.android.widget.control.RadioGroupOption

abstract class AbsSettingsActivity(
    private val dividerMode: DividerMode,
    private val indentEverything: Boolean
) : AppCompatActivity() {

    enum class DividerMode {
        ALWAYS,
        NEVER,
        AFTER_SECTION,
        IN_SECTION
    }

    abstract val llItemContainer: LinearLayout

    // inflate

    /**
     * Call [inflateSections] here.
     */
    protected abstract fun inflateSettingItems()

    protected fun reflateViews() {
        llItemContainer.removeAllViews()
        inflateSettingItems()
    }

    protected fun inflateSections(vararg sections: ItemSection?) {
        for ((index, section) in sections.withIndex()) {
            // safecall to allow passing items with takeIf(), allowing to check for e.g. developer mode
            section?.inflate(isLastSection = index == sections.size - 1)
        }
    }

    // item section

    protected inner class ItemSection(
        val title: String,
        private vararg val items: SettingItem?
    ) {
        fun inflate(isLastSection: Boolean) {
            inflateHeader(title)

            for ((index, item) in items.withIndex()) {
                val isLastItem = index == items.size - 1

                // safecall to allow passing items with takeIf(), allowing to check for e.g. developer mode
                item?.inflate(
                    if (isLastSection && isLastItem) true
                    else when (dividerMode) {
                        DividerMode.ALWAYS -> false
                        DividerMode.NEVER -> true
                        DividerMode.IN_SECTION -> isLastItem
                        DividerMode.AFTER_SECTION -> !isLastItem
                    }
                )
            }
        }
    }

    // dialog item

    // non-dialog item

    protected inner class BooleanItem(
        title: String,
        private val descOn: String = "",
        private val descOff: String = descOn,
        private val value: Boolean,
        @DrawableRes iconRes: Int = NO_RES,
        private val onSwitch: (checked: Boolean) -> Unit
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateSwitchItem(title, descOn, descOff, value, hideDivider, iconRes, onSwitch)
        }
    }

    protected open inner class ActionItem(
        title: String,
        protected val desc: String = "",
        @DrawableRes iconRes: Int = NO_RES,
        protected val onClick: () -> Unit
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateClickItem(title, desc, hideDivider, iconRes) {
                onClick()
            }
        }
    }

    protected inner class LaunchItem(
        title: String,
        desc: String = "",
        iconRes: Int = NO_RES,
        onClick: () -> Unit
    ) : ActionItem(title, desc, iconRes, onClick) {

        constructor(
            title: String,
            desc: String = "",
            iconRes: Int = NO_RES,
            activity: Class<*>
        ) : this(title, desc, iconRes, { launchActivity(activity) })

        override fun inflate(hideDivider: Boolean) {
            inflateClickItem(title, desc, hideDivider, iconRes, getResIdByAttr(R.attr.fw_settingItemOpenActivityIcon)) {
                onClick()
            }
        }
    }

    protected inner class LinkItem(
        title: String,
        desc: String = "",
        iconRes: Int = NO_RES,
        onClick: () -> Unit
    ) : ActionItem(title, desc, iconRes, onClick) {

        constructor(
            title: String,
            desc: String = "",
            iconRes: Int = NO_RES,
            link: String
        ) : this(title, desc, iconRes, { openLink(link) })

        override fun inflate(hideDivider: Boolean) {
            inflateClickItem(title, desc, hideDivider, iconRes, getResIdByAttr(R.attr.fw_settingItemOpenLinkIcon)) {
                onClick()
            }
        }
    }

    protected inner class InfoItem(
        title: String,
        private val desc: String = "",
        private val msg: String = "",
        @DrawableRes iconRes: Int = NO_RES,
        @StringRes private val dialogBtnRes: Int,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc, hideDivider, iconRes,
                alertDialog(DialogOption(title, msg, dialogBtnRes, NO_RES, tag = tag))
            )
        }
    }

    protected inner class ConfirmationItem(
        title: String,
        private val desc: String = "",
        private val msg: String = "",
        @DrawableRes iconRes: Int = NO_RES,
        @StringRes private val dialogPosBtnRes: Int,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc, hideDivider, iconRes,
                alertDialog(DialogOption(title, msg, dialogPosBtnRes, tag = tag))
            )
        }
    }

    protected inner class StringItem(
        title: String,
        private val desc: String? = null,
        private val msg: String = "",
        private val value: String,
        private val hint: String,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: value, hideDivider, iconRes,
                inputDialog(
                    DialogOption(title, msg, R.string.fw_dialog_btn_set, tag = tag),
                    InputOption(value, hint)
                )
            )
        }
    }

    protected inner class NumberItem(
        title: String,
        private val desc: String? = null,
        private val msg: String = "",
        private val value: Long? = null,
        private val hint: String,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: value.toString(), hideDivider, iconRes,
                inputDialog(
                    DialogOption(title, msg, R.string.fw_dialog_btn_set, tag = tag),
                    InputOption(value ?: NO_LONG_TEXT, hint)
                )
            )
        }
    }

    protected inner class FloatItem(
        title: String,
        private val desc: String? = null,
        private val msg: String = "",
        private val value: Float,
        private val hint: String,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: value.toString(), hideDivider, iconRes,
                inputDialog(
                    DialogOption(title, msg, R.string.fw_dialog_btn_set, tag = tag),
                    InputOption(value, hint)
                )
            )
        }
    }

    protected inner class SliderItem(
        title: String,
        private val desc: String? = null,
        private val msg: String = "",
        private val min: Float,
        private val max: Float,
        private val step: Float,
        private val value: Float,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: value.toString(), hideDivider, iconRes,
                sliderDialog(
                    DialogOption(title, msg, R.string.fw_dialog_btn_set, tag = tag),
                    min = min, max = max, step = step, value = value
                )
            )
        }
    }

    protected inner class SingleSelectionItem(
        title: String,
        private val desc: String? = null,
        private val values: Array<String>,
        private val selectedIndex: Int,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        val value: String get() = values[selectedIndex]

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: value, hideDivider, iconRes,
                radioDialog(
                    DialogOption(title, posBtnTxtRes = NO_RES, tag = tag),
                    RadioGroupOption(values, selectedIndex)
                )
            )
        }
    }

    protected inner class MultiSelectionItem(
        title: String,
        private val desc: String? = null,
        private val values: Array<String>,
        private val selectedIndices: IntArray,
        @DrawableRes iconRes: Int = NO_RES,
        private val tag: String
    ) : SettingItem(title, iconRes) {

        override fun inflate(hideDivider: Boolean) {
            inflateDialogItem(
                title, desc ?: "", hideDivider, iconRes,
                checkDialog(
                    DialogOption(title, posBtnTxtRes = R.string.fw_dialog_btn_set, tag = tag),
                    CheckListOption(values, selectedIndices)
                )
            )
        }
    }

    //

    abstract inner class SettingItem(
        val title: String,
        @DrawableRes val iconRes: Int
    ) {
        abstract fun inflate(hideDivider: Boolean)
    }

    // inflate item

    private fun inflateHeader(title: String) {
        val itemBinding = FwItemSettingsHeaderBinding.inflate(layoutInflater, llItemContainer, true)
        itemBinding.tv.text = title

        // set start margin
        if (indentEverything) {
            itemBinding.tv.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                // parent to iv + iv width + iv to tv
                marginStart = getDimension(R.dimen.fw_spacing_small).toInt() + 24 +
                    getDimension(R.dimen.fw_spacing_large).toInt()
            }
        }
    }

    protected fun inflateDialogItem(
        title: String,
        value: String,
        hideDivider: Boolean,
        @DrawableRes iconRes: Int,
        dialog: BaseDialog<*>
    ) {
        val itemBinding = inflateTextView(title, value, hideDivider, iconRes)
        itemBinding.root.setOnClickListener {
            dialog.show(supportFragmentManager)
        }
    }

    protected fun inflateSwitchItem(
        title: String,
        descOn: String,
        descOff: String,
        checked: Boolean,
        hideDivider: Boolean,
        @DrawableRes iconRes: Int,
        onSwitch: (checked: Boolean) -> Unit
    ) {
        val desc = if (checked) descOn else descOff
        val itemBinding = inflateSwitchView(title, desc, hideDivider, iconRes)
        itemBinding.sw.isChecked = checked

        itemBinding.root.setOnClickListener {
            itemBinding.sw.isChecked = !itemBinding.sw.isChecked
            onSwitch(itemBinding.sw.isChecked)

            // update desc
            val newDesc = if (itemBinding.sw.isChecked) descOn else descOff
            itemBinding.tvDesc.setTextRemoveIfEmpty(newDesc)
        }
    }

    protected fun inflateClickItem(
        title: String,
        value: String,
        hideDivider: Boolean,
        @DrawableRes iconRes: Int,
        @DrawableRes endIconRes: Int = NO_RES,
        listener: View.OnClickListener?
    ) {
        val itemBinding = inflateTextView(title, value, hideDivider, iconRes, endIconRes)
        itemBinding.root.setOnClickListener(listener)
    }

    // inflate view

    private fun inflateTextView(
        title: String,
        value: String,
        hideDivider: Boolean,
        @DrawableRes iconRes: Int,
        @DrawableRes endIconRes: Int = NO_RES
    ): FwItemSettingsTextBinding =
        FwItemSettingsTextBinding.inflate(layoutInflater, llItemContainer, true).apply {
            // text
            tvTitle.text = title
            tvValue.setTextRemoveIfEmpty(value)

            // view
            root.setItemRipple()
            vDivider.isInvisible = hideDivider

            // icon
            if (iconRes != NO_RES) {
                val icon = getDrawableCompat(iconRes)
                ivIcon.setImageDrawable(icon)
            }
            else ivIcon.hideOrRemove(indentEverything)

            // end icon
            if (endIconRes != NO_RES) {
                val icon = getDrawableCompat(endIconRes)
                ivIconEnd.setImageDrawable(icon)
                ivIconEnd.isVisible = icon != null
            }
            else ivIconEnd.isGone = true
        }

    private fun inflateSwitchView(
        title: String,
        desc: String,
        hideDivider: Boolean,
        @DrawableRes iconRes: Int
    ): FwItemSettingsSwitchBinding =
        FwItemSettingsSwitchBinding.inflate(layoutInflater, llItemContainer, true).apply {

            // text
            tvTitle.text = title
            tvDesc.setTextRemoveIfEmpty(desc)

            // view
            root.setItemRipple()
            vDivider.isInvisible = hideDivider

            // icon
            if (iconRes != NO_RES) {
                val icon = getDrawableCompat(iconRes)
                ivIcon.setImageDrawable(icon)
            }
            else ivIcon.hideOrRemove(indentEverything)
        }
}