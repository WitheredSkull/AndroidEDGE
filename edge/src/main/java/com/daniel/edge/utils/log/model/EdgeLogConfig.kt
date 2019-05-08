package com.daniel.edge.utils.log.model

import com.daniel.edge.utils.system.EdgeSystemUtils

// Create Time 2018/11/1
// Create Author Daniel
/**
 * 1.美化日志输出并提高阅读质量
 * 2.版本发布时可以选择发布来取消Log显示
 * 3.支持Json格式化
 */
class EdgeLogConfig {
    constructor()

    companion object {
        private var instance: EdgeLogConfig? = null
        //日志提示
        var LOG_NAME = "EDGE"
        //设置日志类型
        var TYPE: EdgeLogType = EdgeLogType.RELEASE
        //每一行最大字符长度
        var LINE_MAX_LENGTH: Int = 150
        //上下对齐行数
        var SPACE_LINES: Int = 1
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

    /**
     * 设置日志名
     */
    fun setLogName(s: String): EdgeLogConfig {
        LOG_NAME = s
        return this
    }

    /**
     * 设置日志类型
     */
    fun setType(type: EdgeLogType): EdgeLogConfig {
        TYPE = type
        return this
    }

    /**
     * 设置日志每行最大长度
     */
    fun setLength(int: Int): EdgeLogConfig {
        LINE_MAX_LENGTH = int
        return this
    }

    /**
     * 设置上下间隔
     */
    fun setMarginLines(int: Int): EdgeLogConfig {
        SPACE_LINES = int
        return this
    }

    /**
     * 设置结束语
     */
    fun setEndFlag(s: String): EdgeLogConfig {
        END_FLAG = s
        return this
    }

    /**
     * 设置Release版本自动关闭Log输出
     */
    fun setAutoReleaseCloseLog(flag: Boolean): EdgeLogConfig {
        if (flag) {
            if (!EdgeSystemUtils.isApkInDebug()) {
                setType(EdgeLogType.RELEASE)
            }
        }
        return this
    }
}