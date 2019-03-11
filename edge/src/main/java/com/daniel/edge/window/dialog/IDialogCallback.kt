package com.daniel.edge.window.dialog

import android.app.Dialog
import android.view.View

/**
 * @Author: Daniel
 * @Date: 2018/12/11
 * @Description:
 */
interface IDialogCallback {
    fun onDialogDisplay(v: View?,dialog:Dialog )
    fun onDialogDismiss()
}