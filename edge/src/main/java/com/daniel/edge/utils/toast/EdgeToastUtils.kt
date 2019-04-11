package com.daniel.edge.utils.toast

import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.toast.model.EdgeToastConfig

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
        mToast = Toast(Edge.CONTEXT)
    }

    open fun show(message: Any) {
        var view = View.inflate(Edge.CONTEXT, EdgeToastConfig.LAYOUT_RES, null)
        when (message) {
            is String -> {
                view.findViewById<TextView>(EdgeToastConfig.TEXT_ID_RES).text = message
            }
            is Int -> {
                view.findViewById<TextView>(EdgeToastConfig.TEXT_ID_RES).text = Edge.CONTEXT.getString(message)
            }
        }
        mToast.view = view
        mToast.duration = Toast.LENGTH_SHORT
        mToast.setGravity(Gravity.BOTTOM, 0, EdgeToastConfig.BOTTOM_MARGIN)
        mToast.show()
    }
}