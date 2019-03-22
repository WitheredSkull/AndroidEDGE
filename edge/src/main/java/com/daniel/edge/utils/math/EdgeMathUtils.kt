package com.daniel.edge.utils.math

import java.math.BigDecimal

object EdgeMathUtils {
    /**
     * @param thisNum 需要计算的数据
     * @param totalNum 需要计算的总数
     * @return 返回比率(1/100=0.01)
     */
    fun ratio(thisNum: Int, totalNum: Int): Float {
        return BigDecimal(thisNum).divide(BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP).toFloat()
    }
    /**
     * @param thisNum 需要计算的数据
     * @param totalNum 需要计算的总数
     * @return 返回百分比
     */
    fun percent(thisNum: Int, totalNum: Int): Int {
        return BigDecimal(thisNum).divide(BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).toInt()
    }
}