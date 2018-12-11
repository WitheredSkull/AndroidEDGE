package com.shuanglu.edge.Window.Dialog.Model

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.view.View
import com.shuanglu.edge.Window.Dialog.IDialogCallback

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetConfig {
    var fragmentManagement:FragmentManager? = null
    @LayoutRes var layoutRes:Int = 0
    var layoutView: View? = null
    var dimAmount:Float = 1f
    var iDialogCallback: IDialogCallback? = null
    var tag = "${System.currentTimeMillis()}"

    constructor(fragmentManagement: FragmentManager?, layoutRes: Int) {
        this.fragmentManagement = fragmentManagement
        this.layoutRes = layoutRes
    }

    constructor(fragmentManagement: FragmentManager?, layoutView: View?) {
        this.fragmentManagement = fragmentManagement
        this.layoutView = layoutView
    }

}