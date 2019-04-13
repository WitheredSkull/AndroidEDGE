package com.daniel.edge.utils.time

import android.text.TextUtils
import com.daniel.edge.config.Edge
import com.daniel.edge.R
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:      Daniel
 * @Date:        2018/11/22 17:09
 * @Description:
 *
 */
object EdgeTimeUtils {
    fun getThisWeekday(): String {
        var calendar = Calendar.getInstance()
        val i = calendar.get(Calendar.DAY_OF_WEEK)
        var week = when (i) {
            1 -> Edge.CONTEXT.getString(R.string.sunday)
            2 -> Edge.CONTEXT.getString(R.string.monday)
            3 -> Edge.CONTEXT.getString(R.string.tuesday)
            4 -> Edge.CONTEXT.getString(R.string.wednesday)
            5 -> Edge.CONTEXT.getString(R.string.thursday)
            6 -> Edge.CONTEXT.getString(R.string.friday)
            7 -> Edge.CONTEXT.getString(R.string.saturday)
            else -> {
                ""
            }
        }
        return week
    }


    /**
     * 传入相对应的参数获取相对应的时间，传Null代表不需要，比如
     * EdgeTimeUtils.getFormatTime(System.currentTimeMillis(),"年","月","日",null,null,null)
     * 代表只需要年月日
     */
    fun getFormatTime(
        currentTimeMillis: Long,
        year: String?,
        month: String?,
        day: String?,
        hour: String?,
        minute: String?,
        second: String?
    ): String {
        var format = SimpleDateFormat(
            "${if (!TextUtils.isEmpty(year)) {
                "yyyy$year"
            } else {
                ""
            }}${
            if (!TextUtils.isEmpty(month)) {
                "MM$month"
            } else {
                ""
            }
            }${
            if (!TextUtils.isEmpty(day)) {
                "dd$day"
            } else {
                ""
            }
            } ${
            if (!TextUtils.isEmpty(hour)) {
                "HH$hour"
            } else {
                ""
            }
            }${
            if (!TextUtils.isEmpty(minute)) {
                "mm$minute"
            } else {
                ""
            }
            }${
            if (!TextUtils.isEmpty(second)) {
                "ss$second"
            } else {
                ""
            }
            }"
        )

        val currentTime = Date(currentTimeMillis)
        return format.format(currentTime)
    }

    /**
     * 毫秒格式化为 分钟-秒
     */
    fun millisToMinute_Secoud(
        millisecond: Long
    ): Array<Long> {
        var second = BigDecimal(millisecond).divide(BigDecimal(1000), BigDecimal.ROUND_DOWN, 0).toBigInteger().toLong()
        var minute = (second / 60)
        second = second - minute * 60
        if (second < 0) {
            second = 0
        }
        return arrayOf(minute,second)
    }

    /**
     * 秒格式化为 分钟-秒
     */
    fun secondToMinute_Secoud(
        second: Long
    ): Array<Long> {
        var minute = (second / 60)
        var second1 = second - minute * 60
        if (second1 < 0) {
            second1 = 0
        }
        return arrayOf(minute,second1)
    }

    /**
     * 复杂时间字符串转换为普通的时间字符串
     */
    fun timeToSimpleDate(time: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var date = formatter.parse(time)
        return formatter.format(date)
    }
}