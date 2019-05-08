package com.daniel.edge.utils.sharePreference

import android.content.Context
import android.content.SharedPreferences
import com.daniel.edge.config.Edge
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.management.file.model.EdgeBaseFileProperty
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.text.EdgeTextUtils
import java.io.File
import java.lang.ClassCastException
import java.lang.Exception

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   管理SP工具类
 */
object EdgeSharePreferencesUtils {

    /**
     * @return 获取SharedPreferences
     */
    @JvmStatic
    fun getSP(fileName: String): SharedPreferences {
        return Edge.CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    /**
     * 通过文件名属性名以及基础类型获取属性值
     * 使用方法
     * EdgeSharePreferencesUtils.getSPProperty<Long>("appConfig","xxx",Long)
     * @param fileName 文件名
     * @param property 属性名
     * @param default 默认值
     * @return 返回相对应的泛型
     **/
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getSPProperty(fileName: String, property: String, default: Any): T {
        when (default) {
            is Int ->
                return getSP(fileName).getInt(property, default) as T ?: default as T
            is String ->{
                var value = getSP(fileName).getString(property, default)
                if (EdgeTextUtils.isEmpty(value))
                    return default as T
                else
                    return value as T
            }
            is Long ->
                return getSP(fileName).getLong(property, default) as T ?: default as T
            is Float ->
                return getSP(fileName).getFloat(property, default) as T ?: default as T
            is Boolean ->
                return getSP(fileName).getBoolean(property, default) as T ?: default as T
            else ->
                return default as T
        }
    }

    /**
     * 通过文件名和key and value将属性值写入SharePreferences
     * 使用方法
     * EdgeSharePreferencesUtils.setSPProperty("appConfig","age",1)
     **/
    @JvmStatic
    fun setSPProperty(fileName: String, property: String, value: Any?) {
        val _sp = getSP(fileName)
        val _e = _sp.edit()
        try {
            if (value is String) {
                _e.putString(property, value)
            } else if (value is Int) {
                _e.putInt(property, value)
            } else if (value is Long) {
                _e.putLong(property, value)
            } else if (value is Float) {
                _e.putFloat(property, value)
            } else if (value is Boolean) {
                _e.putBoolean(property, value)
            } else {
                throw ClassCastException("类型不兼容，只支持基础类型，不支持${value!!.javaClass.simpleName}类型")
            }
        } finally {
            _e.apply()
        }
    }

    /**
     * Write more than one at a time
     * 一次存入多个键值对
     * 注意，需要 属性(properties)和值(values)大小一致
     */
    @JvmStatic
    fun setSPProperties(fileName: String, properties: Array<String>, values: Array<Any?>) {
        if (properties.size != values.size) {
            throw ArrayIndexOutOfBoundsException("数组大小不一致 | ${properties.size}!=${values.size} |")
        }
        val _sp = getSP(fileName)
        val _e = _sp.edit()
        try {
            values.forEachIndexed { index, value ->
                val property = properties[index]
                if (value is String) {
                    _e.putString(property, value)
                } else if (value is Int) {
                    _e.putInt(property, value)
                } else if (value is Long) {
                    _e.putLong(property, value)
                } else if (value is Float) {
                    _e.putFloat(property, value)
                } else if (value is Boolean) {
                    _e.putBoolean(property, value)
                } else {
                    throw ClassCastException("类型不兼容，只支持基础类型，不支持${value!!.javaClass.simpleName}类型")
                }
            }
        } finally {
            _e.apply()
        }

    }

    /**
     * 清空指定的SP
     */
    @JvmStatic
    fun clearSP(fileName: String) {
        val sp: SharedPreferences = getSP(fileName)
        sp.edit().clear().apply()
    }

    /**
     * 清空App所有的SP
     */
    @JvmStatic
    fun clearAppAllSP() {
        val list = arrayListOf<EdgeBaseFileProperty>()
        EdgeFileManagement.findDirectoryOnFile(EdgeFileManagement.getSharedPrefsPath(), list);
        list.forEach {
            try {
                if (!it.isDirectory && it.fileName != null) {
                    it.fileName?.substring(0, it.fileName!!.length - 4).apply {
                        clearSP(this!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}