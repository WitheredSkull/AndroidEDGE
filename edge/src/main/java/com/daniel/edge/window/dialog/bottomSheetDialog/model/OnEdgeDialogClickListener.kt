package com.daniel.edge.window.dialog.bottomSheetDialog.model

import android.app.Dialog
import android.view.View

interface OnEdgeDialogClickListener {
    fun onClick(parent:View,view: View,dialog:Dialog )
}