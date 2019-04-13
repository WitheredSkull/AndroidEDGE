package com.daniel.edge.utils.convert

import android.util.TypedValue
import com.daniel.edge.config.Edge

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   转换密度工具
 */
object EdgeDensityUtils {

    /**
     * @return DP转PX
     */
    @JvmStatic
    fun dp2px(dp: Float): Int {
        return (dp * getDensity() + 0.5f).toInt()
    }

    /**
     * @return DP转PX系统转换
     */
    @JvmStatic
    fun dp2pxSystem(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Edge.CONTEXT.resources.displayMetrics)
            .toInt()
    }

    /**
     * @return PX转DP
     */
    @JvmStatic
    fun px2dp(px: Float): Int {
        return (px / getDensity() + 0.5f).toInt()
    }

    /**
     * @return PX转SP
     */
    @JvmStatic
    fun px2sp(px: Float): Int {
        return (px / getScaledDensity() + 0.5f).toInt()
    }

    /**
     * @return SP转PX
     */
    @JvmStatic
    fun sp2px(sp: Float): Int {
        return (sp * getScaledDensity() + 0.5f).toInt()
    }

    /**
     * @return SP转PX系统转换
     */
    @JvmStatic
    fun sp2pxSystem(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Edge.CONTEXT.resources.displayMetrics)
            .toInt()
    }

    /**
     * @return 获取屏幕密度
     */
    private fun getDensity(): Float {
        return Edge.CONTEXT.resources.displayMetrics.density
    }

    /**
     * @return 按比例缩小的密度
     */
    private fun getScaledDensity(): Float {
        return Edge.CONTEXT.resources.displayMetrics.scaledDensity
    }
}