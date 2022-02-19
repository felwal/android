package me.felwal.android.widget.control

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.felwal.android.R
import me.felwal.android.util.asIndicesOfTruths
import me.felwal.android.widget.dialog.BaseDialog

import me.felwal.android.widget.dialog.NO_RES
import me.felwal.android.widget.sheet.BaseSheet

private const val ARG_TITLE = "Title"
private const val ARG_MESSAGE = "Message"
private const val ARG_POS_BTN_RES = "PositiveButtonRes"
private const val ARG_NEG_BTN_RES = "NegativeButtonRes"
private const val ARG_NEU_BTN_RES = "NeutralButtonRes"
private const val ARG_TAG = "Tag"
private const val ARG_PASS_VALUEG = "PassValue"

private const val ARG_LABEL = "Label"
private const val ARG_LABELS = "Labels"
private const val ARG_STATE = "State"
private const val ARG_STATES = "States"
private const val ARG_ICON = "Icon"
private const val ARG_ICONS = "Icons"
private const val ARG_CHECKED_INDEX = "CheckedIndex"
private const val ARG_ASCENDING = "Ascending"

private const val ARG_TEXT = "Text"
private const val ARG_HINT = "Hint"
private const val ARG_INPUT_TYPE = "InputType"

//

abstract class ControlOption

//

class DialogOption(
    val title: String,
    val message: String = "",
    @StringRes val posBtnTxtRes: Int = R.string.fw_dialog_btn_ok,
    @StringRes val negBtnTxtRes: Int = R.string.fw_dialog_btn_cancel,
    @StringRes val neuBtnTxtRes: Int = NO_RES,
    val tag: String,
    val passValue: String? = null
) {
    companion object {
        fun simple(
            title: String,
            message: String = "",
            @StringRes posBtnTxtRes: Int = NO_RES,
            @StringRes negBtnTxtRes: Int = NO_RES,
            @StringRes neuBtnTxtRes: Int = NO_RES,
            tag: String,
            passValue: String? = null
        ) = DialogOption(title, message, posBtnTxtRes, negBtnTxtRes, neuBtnTxtRes, tag, passValue)
    }
}

class SheetOption(
    val title: String,
    val message: String = "",
    val tag: String,
    val passValue: String? = null
)

//

abstract class MultiChoiceOption(
    val label: String,
    val itemState: Boolean,
    @DrawableRes val icon: Int?,
) : ControlOption()

abstract class MultiChoiceListOption(
    val labels: Array<String>,
    val itemStates: BooleanArray,
    @DrawableRes val icons: IntArray?,
) : ControlOption()

abstract class SingleChoiceListOption(
    val labels: Array<String>,
    var checkedIndex: Int,
    @DrawableRes val icons: IntArray?,
) : ControlOption()

//

class SwitchOption(
    label: String,
    itemState: Boolean,
    @DrawableRes icon: Int? = null
) : MultiChoiceOption(label, itemState, icon)

class CheckBoxOption(
    label: String,
    itemState: Boolean,
    @DrawableRes icon: Int? = null
) : MultiChoiceOption(label, itemState, icon)

//

class ListOption(
    val labels: Array<String>,
    @DrawableRes val icons: IntArray? = null,
) : ControlOption()

class CheckListOption(
    labels: Array<String>,
    itemStates: BooleanArray,
    @DrawableRes icons: IntArray? = null
) : MultiChoiceListOption(labels, itemStates, icons) {
    constructor(
        labels: Array<String>,
        checkedIndices: IntArray,
        @DrawableRes icons: IntArray? = null
    ) : this(labels, checkedIndices.asIndicesOfTruths(labels.size), icons)
}

class RadioGroupOption(
    labels: Array<String>,
    checkedIndex: Int,
    @DrawableRes icons: IntArray? = null
) : SingleChoiceListOption(labels, checkedIndex, icons)

class TriRadioGroupOption(
    labels: Array<String>,
    checkedIndex: Int,
    val ascending: Boolean,
) : SingleChoiceListOption(labels, checkedIndex, null)

//

class InputOption(
    val text: String = "",
    val hint: String = "",
    val inputType: Int = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
) : ControlOption() {
    constructor(
        text: Int,
        hint: String
    ) : this(text.toString(), hint, EditorInfo.TYPE_CLASS_NUMBER)

    constructor(
        text: Long,
        hint: String
    ) : this(text.toString(), hint, EditorInfo.TYPE_CLASS_NUMBER)

    constructor(
        text: Float,
        hint: String
    ) : this(text.toString(), hint, EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL)
}

// inflation

fun BaseDialog<*>.inflateList(option: ListOption, ll: LinearLayout, l: (which: Int) -> Unit) =
    setItems(option.labels, option.icons, ll, l)

fun BaseSheet<*>.inflateList(option: ListOption, ll: LinearLayout, l: (which: Int) -> Unit) =
    setItems(option.labels, option.icons, ll, l)

fun BaseDialog<*>.inflateRadioGroup(option: RadioGroupOption, ll: LinearLayout, l: (which: Int) -> Unit) =
    setSingleChoiceItems(option.labels, option.checkedIndex, option.icons, ll, l)

fun BaseSheet<*>.inflateRadioGroup(option: RadioGroupOption, ll: LinearLayout, l: (which: Int) -> Unit) =
    setSingleChoiceItems(option.labels, option.checkedIndex, option.icons, ll, l)

fun BaseDialog<*>.inflateCheckList(option: CheckListOption, ll: LinearLayout, l: (which: Int, Boolean) -> Unit) =
    setMultiChoiceItems(option.labels, option.itemStates, option.icons, ll, l)

fun BaseSheet<*>.inflateCheckList(option: CheckListOption, ll: LinearLayout, l: (which: Int, Boolean) -> Unit) =
    setMultiChoiceItems(option.labels, option.itemStates, option.icons, ll, l)

// bundle

fun Bundle.putDialogOption(key: String, option: DialogOption) {
    putString(key + ARG_TITLE, option.title)
    putString(key + ARG_MESSAGE, option.message)
    putInt(key + ARG_POS_BTN_RES, option.posBtnTxtRes)
    putInt(key + ARG_NEG_BTN_RES, option.negBtnTxtRes)
    putInt(key + ARG_NEU_BTN_RES, option.neuBtnTxtRes)
    putString(key + ARG_TAG, option.tag)
    putString(key + ARG_PASS_VALUEG, option.passValue)
}

fun Bundle.getDialogOption(key: String) = DialogOption(
    title = getString(key + ARG_TITLE, ""),
    message = getString(key + ARG_MESSAGE, ""),
    posBtnTxtRes = getInt(key + ARG_POS_BTN_RES),
    negBtnTxtRes = getInt(key + ARG_NEG_BTN_RES),
    neuBtnTxtRes = getInt(key + ARG_NEU_BTN_RES),
    tag = getString(key + ARG_TAG, ""),
    passValue = getString(key + ARG_PASS_VALUEG)
)

fun Bundle.putSheetOption(key: String, option: SheetOption) {
    putString(key + ARG_TITLE, option.title)
    putString(key + ARG_MESSAGE, option.message)
    putString(key + ARG_TAG, option.tag)
    putString(key + ARG_PASS_VALUEG, option.passValue)
}

fun Bundle.getSheetOption(key: String) = SheetOption(
    title = getString(key + ARG_TITLE, ""),
    message = getString(key + ARG_MESSAGE, ""),
    tag = getString(key + ARG_TAG, ""),
    passValue = getString(key + ARG_PASS_VALUEG)
)

fun Bundle.putCheckBoxOption(key: String, option: CheckBoxOption) {
    putString(key + ARG_LABEL, option.label)
    putBoolean(key + ARG_STATE, option.itemState)
    option.icon?.let { putInt(key + ARG_ICON, it) }
}

fun Bundle.getCheckBoxOption(key: String) = CheckBoxOption(
    getString(key + ARG_LABEL, ""),
    getBoolean(key + ARG_STATE),
    getInt(key + ARG_ICON)
)

fun Bundle.putListOption(key: String, option: ListOption) {
    putStringArray(key + ARG_LABELS, option.labels)
    putIntArray(key + ARG_ICONS, option.icons)
}

fun Bundle.getListOption(key: String) = ListOption(
    getStringArray(key + ARG_LABELS) ?: arrayOf(),
    getIntArray(key + ARG_ICONS) ?: intArrayOf()
)

fun Bundle.putSwitchOption(key: String, option: SwitchOption) {
    putString(key + ARG_LABEL, option.label)
    putBoolean(key + ARG_STATE, option.itemState)
    option.icon?.let { putInt(key + ARG_ICON, it) }
}

fun Bundle.getSwitchOption(key: String) = SwitchOption(
    getString(key + ARG_LABEL, ""),
    getBoolean(key + ARG_STATE),
    getInt(key + ARG_ICON)
)

fun Bundle.putCheckListOption(key: String, option: CheckListOption) {
    putStringArray(key + ARG_LABELS, option.labels)
    putBooleanArray(key + ARG_STATES, option.itemStates)
    putIntArray(key + ARG_ICONS, option.icons)
}

fun Bundle.getCheckListOption(key: String) = CheckListOption(
    getStringArray(key + ARG_LABELS) ?: arrayOf(),
    getBooleanArray(key + ARG_STATES) ?: booleanArrayOf(),
    getIntArray(key + ARG_ICONS) ?: intArrayOf()
)

fun Bundle.putRadioGroupOption(key: String, option: RadioGroupOption) {
    putStringArray(key + ARG_LABELS, option.labels)
    putInt(key + ARG_CHECKED_INDEX, option.checkedIndex)
    putIntArray(key + ARG_ICONS, option.icons)
}

fun Bundle.getRadioGroupOption(key: String) = RadioGroupOption(
    getStringArray(key + ARG_LABELS) ?: arrayOf(),
    getInt(key + ARG_CHECKED_INDEX),
    getIntArray(key + ARG_ICONS) ?: intArrayOf()
)

fun Bundle.putTriRadioGroupOption(key: String, option: TriRadioGroupOption) {
    putStringArray(key + ARG_LABELS, option.labels)
    putInt(key + ARG_CHECKED_INDEX, option.checkedIndex)
    putBoolean(key + ARG_ASCENDING, option.ascending)
}

fun Bundle.getTriRadioGroupOption(key: String) = TriRadioGroupOption(
    getStringArray(key + ARG_LABELS) ?: arrayOf(),
    getInt(key + ARG_CHECKED_INDEX),
    getBoolean(key + ARG_ASCENDING),
)

fun Bundle.putInputOption(key: String, option: InputOption) {
    putString(key + ARG_TEXT, option.text)
    putString(key + ARG_HINT, option.hint)
    putInt(key + ARG_INPUT_TYPE, option.inputType)
}

fun Bundle.getInputOption(key: String) = InputOption(
    getString(key + ARG_TEXT, ""),
    getString(key + ARG_HINT, ""),
    getInt(key + ARG_INPUT_TYPE, EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES)
)