package com.daniel.edge.utils.log

import android.text.TextUtils
import android.util.Log
import com.daniel.edge.utils.log.model.EdgeLogConfig
import com.daniel.edge.utils.log.model.EdgeLogModule
import com.daniel.edge.utils.log.model.EdgeLogType
import com.daniel.edge.utils.text.EdgeTextUtils
import java.lang.Exception
import kotlin.text.StringBuilder

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeLog {

    @JvmStatic
    fun d(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogType.DEBUG, clazz, tag, message)
    }

    @JvmStatic
    fun e(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogType.ERROR, clazz, tag, message)
    }

    @JvmStatic
    fun i(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogType.INFO, clazz, tag, message)
    }

    @JvmStatic
    fun show(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogConfig.TYPE, clazz, tag, message)
    }

    @JvmStatic
    fun showJson(clazz: Class<*>?, tag: String, message: String) {
        show(clazz, tag, EdgeTextUtils.formatJson(message))
    }

    @JvmStatic
    fun v(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogType.VERBOSE, clazz, tag, message)
    }

    @JvmStatic
    fun w(clazz: Class<*>?, tag: String, vararg message: Any?) {
        show(EdgeLogType.WARN, clazz, tag, message)
    }

    @JvmStatic
    private fun show(type: EdgeLogType, clazz: Class<*>?, tag: String, message: Array<out Any?>) {
        if (type != EdgeLogType.RELEASE) {
            try {
                if (message.size == 0) {
                    EdgeLogModule.show(type, clazz ?: this::class.java, tag, arrayOf("empty"))
                } else {
                    EdgeLogModule.show(type, clazz ?: this::class.java, tag, message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}