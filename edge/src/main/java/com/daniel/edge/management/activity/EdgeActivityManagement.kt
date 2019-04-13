package com.daniel.edge.management.activity

import android.app.Activity
import androidx.annotation.NonNull

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   Activity管理器
 */
class EdgeActivityManagement {

    private var activities: ArrayList<Activity> = arrayListOf()
    /**
     * 添加Activity
     */
    fun add(activity: Activity?) {
        activities.add(activity!!)
    }

    /**
     * 退出所有的活动
     */
    fun exit() {
        activities.forEach {
            it.finish()
        }
    }

    /**
     * 获取Top Activity
     */
    fun findTopActivity(): Activity {
        return activities.get(activities.size - 1)
    }

    /**
     * 移除指定Class的Activity
     */
    fun finishOnClass(clazz: Class<*>) {
        activities.forEach {
            if (clazz.simpleName.equals(it.javaClass.simpleName)) {
                it.finish()
                return@forEach
            }
        }
    }

    /**
     * 移除最后多少个Activities
     */
    fun finishOnLastFew(num: Int) {
        if (activities.size > 0 && num < activities.size) {
            val start = (activities.size) - num
            for (i in start..activities.size - 1) {
                activities.get(i).finish()
            }
        }
    }

    /**
     * 移除指定位置的Activity
     */
    fun finishOnPosition(position: Int) {
        if (position < activities.size && position > 0) {
            activities.get(position).finish()
        } else {
            throw ArrayIndexOutOfBoundsException("超出指定位置或低于0")
        }
    }

    /**
     * 移除指定名字的Activity
     */
    fun finishOnSimpleName(@NonNull simpleName: String) {
        activities.forEach {
            if (simpleName.equals(it.javaClass.simpleName)) {
                it.finish()
                return@forEach
            }
        }
    }

    /**
     * @return 通过类名获取Activity
     */
    @Suppress("UNCHECKED_CAST")
    fun <A : Activity> getActivity(clazz: Class<*>): A? {
        var activity: Activity? = null
        activities.forEachIndexed { _, a ->
            if (clazz.simpleName.equals(a.javaClass.simpleName)) {
                activity = a
                return@forEachIndexed
            }
        }
        return activity as A
    }

    /**
     * @return 获取当前Activity在Activity栈的位置
     */
    fun getActivityPosition(clazz: Class<*>): Int {
        var position = -1
        activities.forEachIndexed { index, activity ->
            if (clazz.simpleName.equals(activity.javaClass.simpleName)) {
                position = index
                return@forEachIndexed
            }
        }
        return position
    }

    /**
     * @return 获取总Activity数量
     */
    fun getSize(): Int {
        return activities.size
    }

    /**
     * 移除Activity
     */
    fun remove(activity: Activity?) {
        activities.remove(activity)
    }

    //单例模式
    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    private object Instance {
        val INSTANCE = EdgeActivityManagement()
    }
}