package com.daniel.edge.config

import android.annotation.SuppressLint
import android.content.Context

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   xxx
 */
@SuppressLint("StaticFieldLeak")
object Edge {
    @JvmStatic
    lateinit var CONTEXT:Context
    @JvmStatic
    var DATABASE_VERSION = 1

    /**
     * 网络请求相关
     */
    val CONNECT_TIMEOUT = 5000
    val READ_TIMEOUT = 5000
}