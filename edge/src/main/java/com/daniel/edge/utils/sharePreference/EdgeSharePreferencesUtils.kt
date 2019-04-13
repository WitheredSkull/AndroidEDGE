package com.daniel.edge.utils.sharePreference

import android.content.Context
import android.content.SharedPreferences
import com.daniel.edge.config.Edge
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.management.file.model.EdgeBaseFileProperty
import java.lang.ClassCastException

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
                return getSP(fileName).getInt(property, default) as T;
            is String ->
                return getSP(fileName).getString(property, default) as T;
            is Long ->
                return getSP(fileName).getLong(property, default) as T;
            is Float ->
                return getSP(fileName).getFloat(property, default) as T;
            is Boolean ->
                return getSP(fileName).getBoolean(property, default) as T;
            else ->
                return null as T
        }
    }

    /**
     * 通过文件名和key and value将属性值写入SharePreferences
     * 使用方法
     * EdgeSharePreferencesUtils.setSPProperty("appConfig","age",1)
     **/
    @JvmStatic
    fun setSPProperty(fileName: String, property: String, value: Any?) {
        if (value is String) {
            getSP(fileName).edit().putString(property, value).commit()
        } else if (value is Int) {
            getSP(fileName).edit().putInt(property, value).commit()
        } else if (value is Long) {
            getSP(fileName).edit().putLong(property, value).commit()
        } else if (value is Float) {
            getSP(fileName).edit().putFloat(property, value).commit()
        } else if (value is Boolean) {
            getSP(fileName).edit().putBoolean(property, value).commit()
        } else {
            throw ClassCastException("类型不兼容，只支持基础类型，不支持${value!!.javaClass.simpleName}类型")
        }
    }

    /**
     * 清空指定的SP
     */
    @JvmStatic
    fun clearSP(fileName: String) {
        var sp: SharedPreferences = getSP(fileName)
        sp.edit().clear().commit()
    }

    /**
     * 清空App所有的SP
     */
    @JvmStatic
    fun clearAppAllSP() {
        var list = arrayListOf<EdgeBaseFileProperty>()
        EdgeFileManagement.findDirectoryOnFile(EdgeFileManagement.getSharedPrefsPath(), list);
        list.forEach {
            clearSP(it.fileName!!)
        }
    }
}