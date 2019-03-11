package com.daniel.edge.utils.log

import android.text.TextUtils
import android.util.Log
import com.daniel.edge.utils.log.model.EdgeLogConfig
import com.daniel.edge.utils.log.model.EdgeLogModule
import com.daniel.edge.utils.log.model.EdgeLogType
import com.daniel.edge.utils.text.EdgeTextUtils
import java.lang.Exception

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeLog {
    @JvmStatic
    fun show(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogConfig.TYPE, clazz, tag, message)
    }

    @JvmStatic
    fun showJson(clazz: Class<*>?, tag: String, message: String?) {
        show(clazz, tag, EdgeTextUtils.formatJson(message!!))
    }


    @JvmStatic
    fun v(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogType.VERBOSE, clazz, tag, message)
    }

    @JvmStatic
    fun d(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogType.DEBUG, clazz, tag, message)
    }


    @JvmStatic
    fun i(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogType.INFO, clazz, tag, message)
    }

    @JvmStatic
    fun w(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogType.WARN, clazz, tag, message)
    }

    @JvmStatic
    fun e(clazz: Class<*>?, tag: String, message: String?) {
        show(EdgeLogType.ERROR, clazz, tag, message)
    }

    @JvmStatic
    private fun show(type: EdgeLogType, clazz: Class<*>?, tag: String, message: String?) {
        var value = message
        if (TextUtils.isEmpty(value)) {
            value = "null"
        }
        if (clazz != null )
            if (type != EdgeLogType.RELEASE) {
                try {
                    EdgeLogModule.show(type, clazz, tag, value!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else
                Log.w("错误", "EdgeLog-->日志系统找不到足够的参数")
    }
}