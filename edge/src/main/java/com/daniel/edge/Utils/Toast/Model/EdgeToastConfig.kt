package com.daniel.edge.Utils.Toast.Model

import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.daniel.edge.Utils.Convert.EdgeDensityUtils
import com.daniel.edge.R

/**
 * @Author:      Daniel
 * @Date:        2018/11/29 10:02
 * @Description: 初始化Toast
 *
 */
class EdgeToastConfig {
    companion object {
        @LayoutRes
        var LAYOUT_RES: Int = R.layout.layout_toast
        @IdRes
        var TEXT_ID_RES: Int = R.id.tv_message
        var BOTTOM_MARGIN = EdgeDensityUtils.dp2px(20f)
        fun getInstance() = Instance.INSTANCE
    }

    object Instance {
        val INSTANCE = EdgeToastConfig()
    }

    fun setLayout(@LayoutRes layoutRes: Int): EdgeToastConfig {
        LAYOUT_RES = layoutRes
        return this
    }

    fun setTextId(@IdRes idRes: Int): EdgeToastConfig {
        TEXT_ID_RES = idRes
        return this
    }

    fun setBottomMargin(@Dimension dp: Int): EdgeToastConfig {
        BOTTOM_MARGIN = EdgeDensityUtils.dp2px(dp.toFloat())
        return this
    }

    fun build() {

    }
}