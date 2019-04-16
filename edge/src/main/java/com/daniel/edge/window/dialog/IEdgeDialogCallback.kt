package com.daniel.edge.window.dialog

import android.app.Dialog
import android.view.View
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment

/**
 * @Author: Daniel
 * @Date: 2018/12/11
 * @Description:
 */
interface IEdgeDialogCallback {
    fun onDialogDisplay(v: View?,dialog:EdgeBottomSheetDialogFragment )
    fun onDialogDismiss()
}