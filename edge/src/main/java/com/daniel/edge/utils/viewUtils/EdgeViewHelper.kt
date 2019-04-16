package com.daniel.edge.utils.viewUtils

import android.app.Dialog
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.daniel.edge.R
import com.daniel.edge.window.dialog.IEdgeDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener

/**
 * @Author:      Daniel
 * @Date:        2018/11/28 17:13
 * @Description:
 *
 */
object EdgeViewHelper {

    private var upClickTime = 0L
    /**
     * 添加大批量的文字改变监听
     */
    fun addTextChangeListens(textWatcher: TextWatcher, vararg textView: TextView) {
        textView.forEach {
            it.addTextChangedListener(textWatcher)
        }
    }

    /**
     * @param interval 时间间隔，毫秒
     * @return 防重复点击
     */
    fun  preventRepeatClick(interval: Long): Boolean {
        val thisTime = System.currentTimeMillis()
        if (thisTime - upClickTime >= interval) {
            upClickTime = thisTime
            return true
        } else {
            return false
        }
    }

    //大批量移除的文字改变监听
    fun removeTextChangeListens(textWatcher: TextWatcher, vararg textView: TextView) {
        textView.forEach {
            it.removeTextChangedListener(textWatcher)
        }
    }

    /**
     * 设置大批量的点击事件
     */
    fun setOnClicks(onClickListener: View.OnClickListener, vararg view: View) {
        view.forEach {
            it.setOnClickListener(onClickListener)
        }
    }

    /**
     * 设置大批量的ViewGroup点击事件
     */
    fun setOnClicks(onClickListener: View.OnClickListener, vararg view: ViewGroup) {
        view.forEach {
            it.setOnClickListener(onClickListener)
        }
    }


    //大批量的验证文字是否为空
    fun verifyNotEmpty(vararg textView: TextView): Boolean {
        textView.forEach {
            if (TextUtils.isEmpty(it.text.toString())) {
                return false
            }
        }
        return true
    }

    //大批量验证输入框文字长度
    fun verifyTextLength(len: Int, vararg textView: TextView): Boolean {
        textView.forEach {
            if (TextUtils.isEmpty(it.text.toString()) || it.length() < len) {
                return false
            }
        }
        return true
    }
}