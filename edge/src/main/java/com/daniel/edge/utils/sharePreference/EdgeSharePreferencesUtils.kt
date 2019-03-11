package com.daniel.edge.utils.sharePreference

import android.content.Context
import android.content.SharedPreferences
import com.daniel.edge.config.EdgeConfig
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.management.file.model.EdgeBaseFileProperty
import java.lang.ClassCastException

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   管理SP工具类
 */
object EdgeSharePreferencesUtils {

    //获取SharedPreferences
    @JvmStatic
    fun getSP(fileName: String): SharedPreferences {
        return EdgeConfig.CONTEXT.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    //通过文件名属性名以及基础类型获取属性值
    /*
    使用方法
    EdgeSharePreferencesUtils.getSPProperty<Long>("appConfig","xxx",Long)
    */
    @JvmStatic
    fun <T> getSPProperty(fileName: String, property: String, type: Any): T {
        when (type) {
            Int ->
                return getSP(fileName).getInt(property, 0) as T;
            String ->
                return getSP(fileName).getString(property, "") as T;
            Long ->
                return getSP(fileName).getLong(property, 0) as T;
            Float ->
                return getSP(fileName).getFloat(property, 0f) as T;
            Boolean ->
                return getSP(fileName).getBoolean(property, false) as T;
            else ->
                return null as T
        }
    }

    /*
    通过文件名和key and value将属性值写入SharePreferences
    使用方法
    EdgeSharePreferencesUtils.setSPProperty("appConfig","age",1)
    */
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

    //清空指定的SP
    @JvmStatic
    fun clearSP(fileName: String){
        var sp:SharedPreferences = getSP(fileName)
        sp.edit().clear().commit()
    }

    //清空App所有的SP
    @JvmStatic
    fun clearAppSP(){
        var list = arrayListOf<EdgeBaseFileProperty>()
        EdgeFileManagement.findDirectoryOnFile(EdgeFileManagement.getSharedPrefsPath(),list);
        list.forEach {
            clearSP(it.fileName!!)
        }
    }
}