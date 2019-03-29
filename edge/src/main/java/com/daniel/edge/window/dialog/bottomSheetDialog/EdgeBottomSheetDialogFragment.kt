package com.daniel.edge.window.dialog.bottomSheetDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.daniel.edge.R
import com.daniel.edge.window.dialog.IDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener
import java.lang.Exception

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    override fun onClick(v: View) {
        mOnClickListener?.onClick(mView!!,v, dialog)
    }

    //Dialog
    private var bottomSheetDialog: BottomSheetDialog? = null
    //打开次数
    private var startCount = 0
    //管理器
    private var fm: FragmentManager? = null
    //View
    private var mView: View? = null
    //布局
    private var mLayout: Int? = null
    //透明度
    private var mDimAmount = 0f
    //Dialog现实和关闭回调
    private var mDialogCallback: IDialogCallback? = null
    //点击事件Id集合
    private var mOnClickIdRes = arrayListOf<Int>()
    //点击事件
    private var mOnClickListener: OnEdgeDialogClickListener? = null

    companion object {
        /**
         * @return 返回BottomSheetDialogFragment
         * @param fm 管理器
         * @param layoutRes 布局
         */
        fun build(fm: FragmentManager, layoutRes: Int): EdgeBottomSheetDialogFragment {
            val dialogFragment = EdgeBottomSheetDialogFragment()
            dialogFragment.mLayout = layoutRes
            dialogFragment.fm = fm
            return dialogFragment
        }

        /**
         * @return 返回BottomSheetDialogFragment
         * @param fm 管理器
         * @param layoutView 布局
         */
        fun build(fm: FragmentManager, layoutView: View): EdgeBottomSheetDialogFragment {
            val dialogFragment = EdgeBottomSheetDialogFragment()
            dialogFragment.mView = layoutView
            dialogFragment.fm = fm
            return dialogFragment
        }
    }

    /**
     * @param callback 设置Dialog的回调
     */
    fun setDialogCallback(callback: IDialogCallback): EdgeBottomSheetDialogFragment {
        mDialogCallback = callback
        return this
    }

    /**
     * @param dimAmount 界面透明度
     */
    fun setDimAmount(dimAmount: Float): EdgeBottomSheetDialogFragment {
        mDimAmount = dimAmount
        bottomSheetDialog?.window?.setDimAmount(mDimAmount)
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //设置主题样式和对象
        bottomSheetDialog =
            BottomSheetDialog(context!!, R.style.TransparencyBottomSheetDialog)
        if (mView == null && mLayout == null) {
            throw Exception("没有Build或没有传入正确的视图\nNo EdgeBottomSheetDialogFragment.build Or not pass in the specified parameter!!!")
        }
        //初始化View
        if (mView == null) {
            mView = View.inflate(
                context,
                mLayout!!, null
            )
        }
        bottomSheetDialog?.setContentView(mView!!)
        //初始化点击事件
        setOnClick()
        //初始化透明度
        bottomSheetDialog?.window?.setDimAmount(mDimAmount)
        return bottomSheetDialog!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDialogCallback?.onDialogDisplay(mView, dialog)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        mDialogCallback?.onDialogDismiss()
    }

    /**
     * 设置点击事件
     * @param onClickListener 点击回调接口
     * @param resIds 资源ID
     */
    fun addOnClick(onClickListener: OnEdgeDialogClickListener, @IdRes vararg resIds: Int): EdgeBottomSheetDialogFragment {
        mOnClickListener = onClickListener
        mOnClickIdRes.addAll(resIds.toList())
        setOnClick()
        return this
    }

    private fun setOnClick() {
        mView?.let {
            mOnClickIdRes.forEach {
                mView!!.findViewById<View>(it)?.setOnClickListener(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (startCount == 0) {
            ++startCount
//            var bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
//            var behavior = BottomSheetBehavior.from(bottomSheet)
//            if (config!!.isFullScreen) {
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//            if (config!!.maxHeight != 0) {
//                behavior.peekHeight = config!!.maxHeight
//            }
        }
    }

    fun show(tag: String? = null) {
        if (TextUtils.isEmpty(tag)) {
            return show(fm, "${System.currentTimeMillis()}")
        } else {
            return show(fm, tag)
        }
    }

}