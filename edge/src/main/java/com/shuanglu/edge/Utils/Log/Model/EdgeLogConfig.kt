package com.daniel.edge.Utils.Log.Model

import com.shuanglu.edge.Utils.AppCompat.EdgeAppCompat
import com.shuanglu.edge.Utils.System.EdgeSystemUtils

// Create Time 2018/11/1
// Create Author Daniel 
class EdgeLogConfig {
    constructor()

    companion object {
        private var instance: EdgeLogConfig? = null
        //日志提示
        var LOG_NAME = "EDGE"
        //设置日志类型
        var TYPE: EdgeLogType = EdgeLogType.RELEASE
        //每一行最大字符长度
        var LENGTH: Int = 150
        //上下对齐行数
        var LINES: Int = 1
        //起始表情
        var HEAD_TEXT = "⊙﹏⊙∥ "
        //结束标识符
        var END_FLAG = " END"

        @JvmStatic
        @Synchronized
        fun build(): EdgeLogConfig {
            if (instance == null) {
                instance = EdgeLogConfig()
            }
            return instance as EdgeLogConfig
        }
    }

    fun setLogName(s: String): EdgeLogConfig {
        LOG_NAME = s
        return this
    }

    fun setType(type: EdgeLogType): EdgeLogConfig {
        TYPE = type
        return this
    }

    fun setLength(int: Int): EdgeLogConfig {
        LENGTH = int
        return this
    }

    fun setMarginLines(int: Int): EdgeLogConfig {
        LINES = int
        return this
    }

    fun setHeadText(s: String): EdgeLogConfig {
        HEAD_TEXT = s
        return this
    }

    fun setEndFlag(s: String): EdgeLogConfig {
        END_FLAG = s
        return this
    }

    fun setAutoReleaseCloseLog(flag: Boolean): EdgeLogConfig {
        if (flag) {
            if (!EdgeSystemUtils.isApkInDebug()) {
                setType(EdgeLogType.RELEASE)
            }
        }
        return this
    }
}