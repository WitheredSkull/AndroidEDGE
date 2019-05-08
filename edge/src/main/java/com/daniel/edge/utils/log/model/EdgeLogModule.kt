package com.daniel.edge.utils.log.model

import android.util.Log
import com.daniel.edge.utils.text.EdgeTextUtils
import java.lang.StringBuilder

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeLogModule {
    @JvmStatic
    fun show(type: EdgeLogType, clazz: Class<*>, tag: String, message: Array<out Any?>) {
        val content = StringBuilder()
        message.forEachIndexed { index, any ->
            content.append(any?:"empty")
            if (index != message.size-1) {
                content.append("|")
            }
        }
        if (content.length > 1) {
            content.substring(0, content.length - 2)
        }
        val afterContent = editContent(content.toString())
        val string =
            editFirstLine("Class:${clazz.simpleName}|Tag:$tag") + afterContent + editEndLine("总字数:${afterContent.length}|Class:${clazz.simpleName}")
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
    private fun editFirstLine(clazzNameAndTag: String): String {
        var tagLength = EdgeTextUtils.textNum(clazzNameAndTag)
        val tag = if ((tagLength % 2).toInt() == 0) {
            clazzNameAndTag
        } else {
            ++tagLength
            "$clazzNameAndTag="
        }
        val amount = (EdgeLogConfig.LINE_MAX_LENGTH - tagLength) / 2
        val string = StringBuffer()
        string.append(" \n")
        for (i in 0..EdgeLogConfig.SPACE_LINES) {
            string.append("\n")
        }
        for (i in 0..(amount - 1)) {
            if (i == amount - 1) {
                string.append(tag)
            } else {
                string.append("=")
            }
        }
        for (i in 0..(amount - 1)) {
            if (i <= amount - 1)
                string.append("=")
        }
        string.append("\n")
        return string.toString()
    }

    @JvmStatic
    private fun editContent(s: String): String {
        val content = StringBuffer()
        var beUseLength = 0
        s.forEach {
            content.append(it)
            if (EdgeTextUtils.isChinese(it.toString())) {
                beUseLength += 2
                val mod = beUseLength % EdgeLogConfig.LINE_MAX_LENGTH
                if (mod == 0 || mod == 1) {
                    content.append("\n")
                }
            } else {
                ++beUseLength
                val mod = beUseLength % EdgeLogConfig.LINE_MAX_LENGTH
                if (mod == 0) {
                    content.append("\n")
                }
            }
        }
        content.append("\n")
        return content.toString()
    }

    @JvmStatic
    private fun editEndLine(tag: String): String {
        val tag2 = tag + EdgeLogConfig.END_FLAG
        var tag2Length = EdgeTextUtils.textNum(tag2)
        val endTag: String = if (tag2Length.toInt() % 2 == 0) {
            tag2
        } else {
            ++tag2Length
            tag + EdgeLogConfig.END_FLAG + "="
        }
        val amount = (EdgeLogConfig.LINE_MAX_LENGTH - tag2Length) / 2
        val string = StringBuffer()
        for (i in 0..(amount - 1)) {
            if (i == amount - 1) {
                string.append(endTag)
            } else {
                string.append("=")
            }
        }
        for (i in 0..(amount - 1)) {
            if (i <= amount - 1)
                string.append("=")
        }
        for (i in 0..EdgeLogConfig.SPACE_LINES) {
            string.append("\n ")
        }
        return string.toString()
    }
}