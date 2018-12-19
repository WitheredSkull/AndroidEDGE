package com.shuanglu.edge.Window.Dialog.BottomSheetDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shuanglu.edge.R
import com.shuanglu.edge.Window.Dialog.Model.EdgeBottomSheetConfig

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    var config: EdgeBottomSheetConfig? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var count = 0

    companion object {
        fun build(config: EdgeBottomSheetConfig): EdgeBottomSheetDialogFragment {
            var dialogFragment = EdgeBottomSheetDialogFragment()
            dialogFragment.config = config
            return dialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog = BottomSheetDialog(context!!, R.style.TransparencyBottomSheetDialog)
        if (config != null) {
            bottomSheetDialog!!.window.setDimAmount(config!!.dimAmount)
        }
        if (config?.layoutView != null) {
            bottomSheetDialog?.setContentView(config?.layoutView)
        } else {
            bottomSheetDialog?.setContentView(config?.layoutRes!!)
        }
        return bottomSheetDialog!!
    }

    override fun onStart() {
        super.onStart()
        if (count == 0) {
            count++
            var behavior = BottomSheetBehavior.from<View>(view)
            config?.iDialogCallback?.onDialogDisplay(view, behavior, dialog)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        config?.iDialogCallback?.onDialogDismiss()
    }

    fun show() {
        return show(config!!.fragmentManagement, config?.tag)
    }

    override fun show(transaction: FragmentTransaction?, tag: String?): Int {
        return super.show(transaction, tag)
    }

}