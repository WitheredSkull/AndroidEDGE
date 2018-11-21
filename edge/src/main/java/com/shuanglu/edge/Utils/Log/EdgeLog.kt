package com.daniel.edge.Utils.Log

import android.text.TextUtils
import android.util.Log
import com.daniel.edge.Utils.Log.Model.EdgeLogConfig
import com.daniel.edge.Utils.Log.Model.EdgeLogModule
import com.daniel.edge.Utils.Log.Model.EdgeLogType
import com.daniel.edge.Utils.Text.EdgeTextUtils
import java.lang.Exception
import java.lang.NullPointerException

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeLog {
    @JvmStatic
    fun show(clazz: Class<*>?, message: String?) {
        show(EdgeLogConfig.TYPE, clazz, message)
    }

    @JvmStatic
    fun showJson(clazz: Class<*>?, message: String?) {
        show(clazz, EdgeTextUtils.formatJson(message!!))
    }


    @JvmStatic
    fun v(clazz: Class<*>?, message: String?) {
        show(EdgeLogType.VERBOSE, clazz, message)
    }

    @JvmStatic
    fun d(clazz: Class<*>?, message: String?) {
        show(EdgeLogType.DEBUG, clazz, message)
    }


    @JvmStatic
    fun i(clazz: Class<*>?, message: String?) {
        show(EdgeLogType.INFO, clazz, message)
    }

    @JvmStatic
    fun w(clazz: Class<*>?, message: String?) {
        show(EdgeLogType.WARN, clazz, message)
    }

    @JvmStatic
    fun e(clazz: Class<*>?, message: String?) {
        show(EdgeLogType.ERROR, clazz, message)
    }

    @JvmStatic
    fun normal(type: EdgeLogType, clazz: Class<*>?, message: String?) {
        when (type) {
            EdgeLogType.VERBOSE -> Log.v("${clazz!!.javaClass.simpleName}===", message)
            EdgeLogType.DEBUG -> Log.d("${clazz!!.javaClass.simpleName}===", message)
            EdgeLogType.INFO -> Log.i("${clazz!!.javaClass.simpleName}===", message)
            EdgeLogType.WARN -> Log.w("${clazz!!.javaClass.simpleName}===", message)
            EdgeLogType.ERROR -> Log.e("${clazz!!.javaClass.simpleName}===", message)
            EdgeLogType.RELEASE -> {
            }
        }
    }


    @JvmStatic
    private fun show(type: EdgeLogType, clazz: Class<*>?, message: String?) {
        if (clazz != null && !TextUtils.isEmpty(message))
            if (type != EdgeLogType.RELEASE) {
                try {
                    EdgeLogModule.show(type, clazz, message!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else
                throw NullPointerException("EdgeLog-->日志系统找不到足够的参数")
    }
}