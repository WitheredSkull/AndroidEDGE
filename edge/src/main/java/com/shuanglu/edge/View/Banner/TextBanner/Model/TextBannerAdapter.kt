package com.shuanglu.edge.View.Banner.TextBanner.Model

import android.view.View

/**
 * 创建人 Daniel
 * 时间   2018/11/12.
 * 简介   xxx
 */
/**
 * @param T 泛型
 */
abstract class TextBannerAdapter<T>(list: ArrayList<T>) {
    var list: ArrayList<T>

    init {
        this.list = list
    }

    abstract fun getItem(item: T?, position: Int): View
}