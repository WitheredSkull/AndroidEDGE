package com.daniel.edge.window.dialog.bottomSheetDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniel.edge.R
import com.daniel.edge.window.dialog.model.EdgeBottomSheetConfig

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    var config: EdgeBottomSheetConfig? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var startCount = 0

    companion object {
        fun build(config: EdgeBottomSheetConfig): EdgeBottomSheetDialogFragment {
            var dialogFragment = EdgeBottomSheetDialogFragment()
            dialogFragment.config = config
            return dialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog =
            BottomSheetDialog(context!!, R.style.TransparencyBottomSheetDialog)
        if (config != null) {
            bottomSheetDialog!!.window.setDimAmount(config!!.dimAmount)
        }
        if (config!!.layoutRes != 0) {
            config!!.realView = View.inflate(
                context,
                config?.layoutRes!!, null
            )

        } else if (config!!.layoutView != null) {
            config!!.realView = config!!.layoutView
        }
        bottomSheetDialog?.setContentView(config!!.realView)
        return bottomSheetDialog!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        config?.iDialogCallback?.onDialogDisplay(config!!.realView, dialog)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        config?.iDialogCallback?.onDialogDismiss()
    }

    override fun onStart() {
        super.onStart()
        if (startCount == 0) {
            ++startCount
            var bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            var behavior = BottomSheetBehavior.from(bottomSheet)
            if (config!!.isFullScreen) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            if (config!!.maxHeight != 0) {
                behavior.peekHeight = config!!.maxHeight
            }
        }
    }

    fun show() {
        return show(config!!.fragmentManagement, config?.tag)
    }

    override fun show(transaction: androidx.fragment.app.FragmentTransaction?, tag: String?): Int {
        return super.show(transaction, tag)
    }

}