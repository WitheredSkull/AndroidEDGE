package com.shuanglu.edge.Utils.Time

import android.text.TextUtils
import com.daniel.edge.Config.EdgeConfig
import com.shuanglu.edge.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:      Daniel
 * @Date:        2018/11/22 17:09
 * @Description:
 *
 */
object EdgeTimeUtils {
    fun getWeekday(): String {
//        var format = SimpleDateFormat("yyyy-MM-dd")
        var calendar = Calendar.getInstance()
        val i = calendar.get(Calendar.DAY_OF_WEEK)
        var week = when (i) {
            1 -> EdgeConfig.CONTEXT.getString(R.string.sunday)
            2 -> EdgeConfig.CONTEXT.getString(R.string.monday)
            3 -> EdgeConfig.CONTEXT.getString(R.string.tuesday)
            4 -> EdgeConfig.CONTEXT.getString(R.string.wednesday)
            5 -> EdgeConfig.CONTEXT.getString(R.string.thursday)
            6 -> EdgeConfig.CONTEXT.getString(R.string.friday)
            7 -> EdgeConfig.CONTEXT.getString(R.string.saturday)
            else -> {
                ""
            }
        }
        return week
    }

    fun getFormatTime(
        currentTimeMillis:Long,
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
}