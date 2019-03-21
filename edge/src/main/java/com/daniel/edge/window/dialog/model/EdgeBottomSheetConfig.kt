package com.daniel.edge.window.dialog.model

import androidx.annotation.LayoutRes
import android.view.View
import com.daniel.edge.window.dialog.IDialogCallback

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetConfig {
    var fragmentManagement: androidx.fragment.app.FragmentManager? = null
    @LayoutRes var layoutRes:Int = 0
    var layoutView: View? = null
    var realView:View? = null
    var isFullScreen = false
    var maxHeight = 0
    var dimAmount:Float = 0f
    var iDialogCallback: IDialogCallback? = null
    var tag = "${System.currentTimeMillis()}"

    constructor(fragmentManagement: androidx.fragment.app.FragmentManager?, layoutRes: Int) {
        this.fragmentManagement = fragmentManagement
        this.layoutRes = layoutRes
    }

    constructor(fragmentManagement: androidx.fragment.app.FragmentManager?, layoutView: View?) {
        this.fragmentManagement = fragmentManagement
        this.layoutView = layoutView
    }

}