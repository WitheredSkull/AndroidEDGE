package com.shuanglu.edge.Window.Dialog

import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.view.View

/**
 * @Author: Daniel
 * @Date: 2018/12/11
 * @Description:
 */
interface IDialogCallback {
    fun onDialogDisplay(v: View?,behavior:BottomSheetBehavior<View>?,dialog:Dialog )
    fun onDialogDismiss()
}