package com.shuanglu.edge.Utils.ViewUtils

import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

/**
 * @Author:      Daniel
 * @Date:        2018/11/28 17:13
 * @Description:
 *
 */
object EdgeViewHelperUtils {
    //设置大批量的点击事件
    fun setOnClicks(onClickListener: View.OnClickListener,vararg view: View){
        view.forEach {
            it.setOnClickListener(onClickListener)
        }
    }

    //添加大批量的文字改变监听
    fun addTextChangeListens(textWatcher: TextWatcher,vararg textView: TextView){
        textView.forEach {
            it.addTextChangedListener(textWatcher)
        }
    }

    //大批量移除的文字改变监听
    fun removeTextChangeListens(textWatcher: TextWatcher,vararg textView: TextView){
        textView.forEach {
            it.removeTextChangedListener(textWatcher)
        }
    }

    //大批量的验证文字是否为空
    fun verifyNotEmpty(vararg textView: TextView):Boolean{
        textView.forEach {
            if (TextUtils.isEmpty(it.text.toString())){
                return false
            }
        }
        return true
    }

    //大批量验证输入框文字长度
    fun verifyPwd(len:Int,vararg textView: TextView):Boolean{
        textView.forEach {
            if (TextUtils.isEmpty(it.text.toString()) || it.length()<len){
                return false
            }
        }
        return true
    }
}