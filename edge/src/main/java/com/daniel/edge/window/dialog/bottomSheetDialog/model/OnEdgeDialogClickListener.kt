package com.daniel.edge.window.dialog.bottomSheetDialog.model

import android.view.View
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment

interface OnEdgeDialogClickListener {
    fun onClick(parent:View,view: View,dialog:EdgeBottomSheetDialogFragment )
}