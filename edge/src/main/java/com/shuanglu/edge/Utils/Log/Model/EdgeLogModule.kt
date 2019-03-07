package com.daniel.edge.Utils.Log.Model

import android.util.Log
import com.daniel.edge.Utils.Text.EdgeTextUtils
import java.nio.charset.Charset
import kotlin.math.roundToInt

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeLogModule {
    @JvmStatic
    fun show(type: EdgeLogType, clazz: Class<*>, message: String) {
        var string = editFirstLine(clazz) + editContent(message) + editEndLine(clazz)
        when (type) {
            EdgeLogType.VERBOSE -> Log.v(EdgeLogConfig.LOG_NAME, string)
            EdgeLogType.DEBUG -> Log.v(EdgeLogConfig.LOG_NAME, string)
            EdgeLogType.INFO -> Log.v(EdgeLogConfig.LOG_NAME, string)
            EdgeLogType.WARN -> Log.v(EdgeLogConfig.LOG_NAME, string)
            EdgeLogType.ERROR -> Log.v(EdgeLogConfig.LOG_NAME, string)
            EdgeLogType.RELEASE -> {
            }
        }
    }

    @JvmStatic
    private fun editFirstLine(clazz: Class<*>): String {
        var className: String = if ((clazz.simpleName.length % 2) == 0) {
            clazz.simpleName
        } else
            clazz.simpleName + "="
        var amount = (EdgeLogConfig.LENGTH - className.length) / 2
        var string = StringBuffer()
        string.append(" \n")
        for (i in 0..EdgeLogConfig.LINES) {
            string.append("\n")
        }
        for (i in 0..amount - 1) {
            if (i == amount - 1) {
                string.append(className)
            } else {
                string.append("=")
            }
        }
        for (i in 0..amount - 1) {
            if (i <= amount - 1)
                string.append("=")
        }
        string.append("\n")
        return string.toString()
    }

    @JvmStatic
    private fun editContent(s: String): String {
        var content = StringBuffer();
        var length = s.length
        var lines = Math.ceil(length.toDouble().div(EdgeLogConfig.LENGTH)).roundToInt()
        if (lines <= 1) {
            return s.plus("\n")
        }
        for (index in 1..lines) {
            content.appendln(
                s.substring(
                    (index - 1) * EdgeLogConfig.LENGTH,
                    if (index >= lines) {
                        s.length
                    } else {
                        index * EdgeLogConfig.LENGTH
                    }
                )
            )
        }
        return content.toString()
    }

    @JvmStatic
    private fun editEndLine(clazz: Class<*>): String {
        var endName: String = if (((clazz.simpleName + EdgeLogConfig.END_FLAG).length % 2) == 0) {
            clazz.simpleName + EdgeLogConfig.END_FLAG
        } else
            clazz.simpleName + EdgeLogConfig.END_FLAG + "="
        var amount = (EdgeLogConfig.LENGTH - endName.length) / 2
        var string = StringBuffer()
        for (i in 0..amount - 1) {
            if (i == amount - 1) {
                string.append(endName)
            } else {
                string.append("=")
            }
        }
        for (i in 0..amount - 1) {
            if (i <= amount - 1)
                string.append("=")
        }
        for (i in 0..EdgeLogConfig.LINES) {
            string.append("\n ")
        }
        return string.toString()
    }
}