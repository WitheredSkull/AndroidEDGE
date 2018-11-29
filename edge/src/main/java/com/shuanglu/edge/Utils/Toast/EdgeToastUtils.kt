package com.shuanglu.edge.Utils.Toast

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.daniel.edge.Config.EdgeConfig
import com.daniel.edge.Utils.Convert.EdgeDensityUtils
import com.shuanglu.edge.R
import com.shuanglu.edge.Utils.Toast.Model.EdgeToastConfig

/**
 * @Author:      Daniel
 * @Date:        2018/11/28 17:23
 * @Description:
 *
 */
class EdgeToastUtils {

    companion object {
        fun getInstance() = Instance.Instance
    }

    private var mToast: Toast

    object Instance {
        val Instance = EdgeToastUtils()
    }

    init {
        mToast = Toast(EdgeConfig.CONTEXT)
    }

    open fun show(message: Any) {
        var view = View.inflate(EdgeConfig.CONTEXT, EdgeToastConfig.LAYOUT_RES, null)
        when (message) {
            is String -> {
                view.findViewById<TextView>(EdgeToastConfig.LAYOUT_RES).text = message
            }
            is Int -> {
                view.findViewById<TextView>(EdgeToastConfig.LAYOUT_RES).text = EdgeConfig.CONTEXT.getString(message)
            }
        }
        mToast.view = view
        mToast.duration = Toast.LENGTH_SHORT
        mToast.setGravity(Gravity.BOTTOM, 0, EdgeToastConfig.BOTTOM_MARGIN)
        mToast.show()
    }
}