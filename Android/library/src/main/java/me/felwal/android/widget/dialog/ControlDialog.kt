package me.felwal.android.widget.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import me.felwal.android.databinding.FwDialogListBinding
import me.felwal.android.widget.control.ControlOption
import me.felwal.android.widget.control.DialogOption
import me.felwal.android.widget.control.SwitchOption
import me.felwal.android.widget.control.getSwitchOption
import me.felwal.android.widget.control.putSwitchOption

abstract class ControlDialog<L : BaseDialog.DialogListener> : BaseDialog<L>() {

    // TODO

    protected fun buildControlDialog(vararg controls: ControlOption): AlertDialog = builder.run {
        val binding = FwDialogListBinding.inflate(inflater)
        setView(binding.root)

        //for (control in controls) control.inflate(binding.fwLl)

        show().apply {
            fixScrollingDialogCustomPanelPadding()
        }
    }
}

private const val ARG_OPTION_SWITCH = "switch"

class SwitchDialog : ControlDialog<SwitchDialog.DialogListener>() {

    private lateinit var switchOption: SwitchOption

    override fun unpackBundle(bundle: Bundle?) {
        bundle?.apply {
            switchOption = getSwitchOption(ARG_OPTION_SWITCH)
        }
    }

    override fun buildDialog() = buildControlDialog(switchOption)

    interface DialogListener : BaseDialog.DialogListener {
        fun onSwitchDialogSubmitted(option: SwitchOption, tag: String, passValue: String?)
    }

    companion object {
        fun newInstance(dialogOption: DialogOption, switchOption: SwitchOption) = SwitchDialog().apply {
            arguments = putBaseBundle(dialogOption).apply {
                putSwitchOption(ARG_OPTION_SWITCH, switchOption)
            }
        }
    }
}