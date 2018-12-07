package com.shuanglu.edge.Management.Activity

import android.app.Activity
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import com.daniel.edge.Utils.Log.EdgeLog

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   Activity管理器
 */
class EdgeActivityManagement {
    var activities: ArrayList<AppCompatActivity> = arrayListOf()

    //单例模式
    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    private object Instance {
        val INSTANCE = EdgeActivityManagement()
    }

    //添加Activity
    fun add(activity: AppCompatActivity?) {
        activities.add(activity!!)
    }

    //移除Activity
    fun remove(activity: AppCompatActivity?) {
        activities.remove(activity)
    }

    //移除最后多少个Activities
    fun finishOnLastFew(num: Int) {
        val start = (activities.size - 1) - num
        for (i in start..activities.size - 1) {
            activities.get(i).finish()
        }
    }

    //移除指定位置的Activity
    fun finishOnPosition(position: Int) {
        if (position < activities.size && position > 0) {
            activities.get(position).finish()
        } else {
            throw ArrayIndexOutOfBoundsException("超出指定位置或低于0")
        }
    }

    //移除指定名字的Activity
    fun finishOnSimpleName(@NonNull simpleName: String) {
        activities.forEach {
            if (simpleName.equals(it.javaClass.simpleName)) {
                it.finish()
                return@forEach
            }
        }
    }

    //移除指定Class的Activity
    fun finishOnClass(clazz: Class<*>) {
        activities.forEach {
            if (clazz.simpleName.equals(it.javaClass.simpleName)) {
                it.finish()
                return@forEach
            }
        }
    }

    //获取Top Activity
    fun findTopActivity(): AppCompatActivity {
        return activities.get(activities.size - 1)
    }
}