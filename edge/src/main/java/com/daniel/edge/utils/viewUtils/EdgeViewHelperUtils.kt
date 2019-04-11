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
object EdgeViewHelperUtils {
    private var upClickTime = 0L

    //设置防重复点击
    fun isClick(time: Long): Boolean {
        var thisTime = System.currentTimeMillis()
        if (thisTime - upClickTime >= time) {
            upClickTime = thisTime
            return true
        } else {
            return false
        }
    }

    //设置大批量的点击事件
    fun setOnClicks(onClickListener: View.OnClickListener, vararg view: View) {
        view.forEach {
            it.setOnClickListener(onClickListener)
        }
    }

    //设置大批量的点击事件
    fun setOnClicks(onClickListener: View.OnClickListener, vararg view: ViewGroup) {
        view.forEach {
            it.setOnClickListener(onClickListener)
        }
    }

    //添加大批量的文字改变监听
    fun addTextChangeListens(textWatcher: TextWatcher, vararg textView: TextView) {
        textView.forEach {
            it.addTextChangedListener(textWatcher)
        }
    }

    //大批量移除的文字改变监听
    fun removeTextChangeListens(textWatcher: TextWatcher, vararg textView: TextView) {
        textView.forEach {
            it.removeTextChangedListener(textWatcher)
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
    fun verifyPwd(len: Int, vararg textView: TextView): Boolean {
        textView.forEach {
            if (TextUtils.isEmpty(it.text.toString()) || it.length() < len) {
                return false
            }
        }
        return true
    }

    fun showBasicsDialog(
        fm: FragmentManager, @LayoutRes layoutRes: Int,
        title: String,
        content: String,
        confirm: String,
        reject: String,
        onConfirmClickListener: View.OnClickListener?,
        onRejectClickListener: View.OnClickListener?
    ) {
        EdgeBottomSheetDialogFragment.build(fm, layoutRes)
            .setTransparencyBottomSheetDialog()
            .setDialogCallback(object : IEdgeDialogCallback {
                override fun onDialogDisplay(v: View?, dialog: Dialog) {
                    v?.findViewById<TextView>(R.id.tv_title)?.text = title
                    v?.findViewById<TextView>(R.id.tv_content)?.text = content
                    v?.findViewById<TextView>(R.id.tv_allow)?.text = confirm
                    v?.findViewById<TextView>(R.id.tv_reject)?.text = reject
                }

                override fun onDialogDismiss() {
                }
            })
            .addOnClick(object : OnEdgeDialogClickListener {
                override fun onClick(parent: View, view: View, dialog: Dialog) {
                    when (view.id) {
                        R.id.tv_allow -> {
                            onConfirmClickListener?.onClick(view)
                        }
                        R.id.tv_reject -> {
                            onRejectClickListener?.onClick(view)
                        }
                    }
                    dialog.dismiss()
                }
            }, R.id.tv_allow, R.id.tv_allow)
            .show()
    }
}