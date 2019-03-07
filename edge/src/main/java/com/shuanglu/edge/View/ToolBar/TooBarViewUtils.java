package com.shuanglu.edge.View.ToolBar;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;

import com.daniel.edge.Utils.Convert.EdgeDensityUtils;
import com.shuanglu.edge.View.ToolBar.Model.ToolBarValue;

public class TooBarViewUtils {
    private static TooBarViewUtils UTILS;

    public static synchronized TooBarViewUtils getInstance() {
        if (UTILS == null) {
            synchronized (TooBarViewUtils.class) {
                if (UTILS == null)
                    UTILS = new TooBarViewUtils();
            }
        }
        return UTILS;
    }

    /**
     * @param color
     * @return 设置背景颜色
     */
    public TooBarViewUtils setBackgroundColor(@ColorInt int color) {
        ToolBarValue.BACKGROUND_COLOR = color;
        return this;
    }

    /**
     * @param color
     * @return 设置文字颜色
     */
    public TooBarViewUtils setTextColor(@ColorInt int color) {
        ToolBarValue.TEXT_COLOR = color;
        return this;
    }

    /**
     * @param color
     * @return 设置底部线条颜色
     */
    public TooBarViewUtils setLineColor(@ColorInt int color) {
        ToolBarValue.LINE_COLOR = color;
        return this;
    }

    /**
     * @param dp
     * @return 设置底部线条高度
     */
    public TooBarViewUtils setLineHeight(@Dimension int dp) {
        ToolBarValue.LINE_HEIGHT = EdgeDensityUtils.dp2px(dp);
        return this;
    }

//    /**
//     * @param drawable
//     * @return 设置返回图片
//     */
//    public TooBarViewUtils setImgBack(@DrawableRes int drawable) {
//        ToolBarValue.IMG_BACK = drawable;
//        return this;
//    }
//
//    /**
//     * @param drawable
//     * @return 设置标题图片
//     */
//    public TooBarViewUtils setImgTitle(@DrawableRes int drawable) {
//        ToolBarValue.IMG_TITLE = drawable;
//        return this;
//    }
//
//    /**
//     * @param drawable
//     * @return 设置更多图片
//     */
//    public TooBarViewUtils setImgMore(@DrawableRes int drawable) {
//        ToolBarValue.IMG_MORE = drawable;
//        return this;
//    }
}
