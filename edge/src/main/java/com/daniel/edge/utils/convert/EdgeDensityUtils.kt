package com.daniel.edge.utils.convert

import android.util.TypedValue
import com.daniel.edge.config.EdgeConfig

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   转换密度工具
 */
object EdgeDensityUtils {
    //DP转PX
    @JvmStatic
    fun dp2px(dp: Float): Int {
        return (dp * getDensity() + 0.5f).toInt()
    }

    //PX转DP
    @JvmStatic
    fun px2dp(px: Float): Int {
        return (px / getDensity() + 0.5f).toInt()
    }

    //SP转PX
    @JvmStatic
    fun sp2px(sp: Float): Int {
        return (sp * getScaledDensity() + 0.5f).toInt()
    }

    //PX转SP
    @JvmStatic
    fun px2sp(px: Float): Int {
        return (px / getScaledDensity() + 0.5f).toInt()
    }

    //DP转PX系统转换
    @JvmStatic
    fun dp2pxSystem(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, EdgeConfig.CONTEXT.resources.displayMetrics)
            .toInt()
    }

    //SP转PX系统转换
    @JvmStatic
    fun sp2pxSystem(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, EdgeConfig.CONTEXT.resources.displayMetrics)
            .toInt()
    }

    private fun getDensity(): Float {
        return EdgeConfig.CONTEXT.resources.displayMetrics.density
    }

    private fun getScaledDensity(): Float {
        return EdgeConfig.CONTEXT.resources.displayMetrics.scaledDensity
    }
}