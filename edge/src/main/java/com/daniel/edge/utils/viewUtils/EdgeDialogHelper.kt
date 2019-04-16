package com.daniel.edge.utils.viewUtils

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.daniel.edge.R
import com.daniel.edge.window.dialog.IEdgeDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener

/**
 * @author Daniel
 * 简单的Dialog辅助工具，支持直接弹出BottomSheet
 * 需要及时关闭，否则会产生内存泄漏
 */
object EdgeDialogHelper {
    /**
     * @param fm Fragment管理器
     * @param title 标题
     * @param content 内容
     * @param confirm 确定内容
     * @param reject 拒绝内容
     * @param onConfirmClickListener 确定事件
     * @param onRejectClickListener 取消事件
     * 简单的BottomSheet弹出框
     */
    fun showBasicsDialog(
        fm: FragmentManager,
        title: String,
        content: String,
        confirm: String,
        reject: String,
        onConfirmClickListener: View.OnClickListener?,
        onRejectClickListener: View.OnClickListener?
    ) {
        EdgeBottomSheetDialogFragment.build(fm, R.layout.dialog_baise)
            .setNotFold()
            .setTransparencyBottomSheetDialog()
            .setDialogCallback(object : IEdgeDialogCallback {
                override fun onDialogDisplay(v: View?, dialog: EdgeBottomSheetDialogFragment) {
                    v?.findViewById<TextView>(R.id.tv_title)?.text = title
                    v?.findViewById<TextView>(R.id.tv_content)?.text = content
                    v?.findViewById<TextView>(R.id.tv_allow)?.text = confirm
                    v?.findViewById<TextView>(R.id.tv_reject)?.text = reject
                }

                override fun onDialogDismiss() {
                }
            })
            .addOnClick(object : OnEdgeDialogClickListener {
                override fun onClick(parent: View, view: View, dialog: EdgeBottomSheetDialogFragment) {
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