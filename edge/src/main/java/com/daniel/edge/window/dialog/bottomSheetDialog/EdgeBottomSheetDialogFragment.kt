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
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.daniel.edge.R
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.window.dialog.IEdgeDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * @Author: Daniel
 * @Date: 2018/12/10
 * @Description:
 */
class EdgeBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    //Dialog
    private var bottomSheetDialog: BottomSheetDialog? = null
    //管理器
    private var fm: FragmentManager? = null
    //透明度
    private var mDimAmount = -1f
    //Dialog现实和关闭回调
    private var mEdgeDialogCallback: IEdgeDialogCallback? = null
    //是否使用透明主题
    private var mIsTransparencyBottomSheetDialog = false
    //布局
    private var mLayout: Int? = null
    //点击事件Id集合
    private var mOnClickIdRes = arrayListOf<Int>()
    //点击事件
    private var mOnEdgeDialogClickListener: OnEdgeDialogClickListener? = null
    //View
    private var mView: View? = null
    //打开次数
    private var startCount = 0
    //是否需要折叠
    private var mIsFold = true

    override fun onClick(v: View) {
        mOnEdgeDialogClickListener?.onClick(mView!!, v, this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            return super.onCreateDialog(savedInstanceState)
        }
        //设置主题样式和对象
        if (mIsTransparencyBottomSheetDialog) {
            bottomSheetDialog =
                BottomSheetDialog(context!!, R.style.TransparencyBottomSheetDialog)
        } else {
            bottomSheetDialog =
                BottomSheetDialog(context!!)
        }
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
        if (mDimAmount != -1f) {
            bottomSheetDialog?.window?.setDimAmount(mDimAmount)
        }
        return bottomSheetDialog!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mEdgeDialogCallback?.onDialogDisplay(mView, this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mEdgeDialogCallback?.onDialogDismiss()
        super.onDismiss(dialog)
    }

    override fun onStart() {
        super.onStart()
        if (startCount == 0) {
            ++startCount
            if (!mIsFold) {
                var bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
                var behavior = BottomSheetBehavior.from(bottomSheet)
//            if (config!!.isFullScreen) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
//            if (config!!.maxHeight != 0) {
//                behavior.peekHeight = config!!.maxHeight
//            }
        }
    }

    override fun onDestroyView() {
        mEdgeDialogCallback = null
        mOnEdgeDialogClickListener = null
        super.onDestroyView()
    }

    /**
     * 设置点击事件
     * @param onClickListener 点击回调接口
     * @param resIds 资源ID
     */
    fun addOnClick(onClickListener: OnEdgeDialogClickListener, @IdRes vararg resIds: Int): EdgeBottomSheetDialogFragment {
        mOnEdgeDialogClickListener = onClickListener
        mOnClickIdRes.addAll(resIds.toList())
        setOnClick()
        return this
    }

    /**
     * @param callbackEdge 设置Dialog的回调
     */
    fun setDialogCallback(callbackEdge: IEdgeDialogCallback): EdgeBottomSheetDialogFragment {
        mEdgeDialogCallback = callbackEdge
        return this
    }

    /**
     * 关闭折叠
     */
    fun setNotFold(): EdgeBottomSheetDialogFragment {
        mIsFold = false
        return this
    }

    /**
     * @param dimAmount Window界面遮罩层透明度
     */
    fun setDimAmount(dimAmount: Float): EdgeBottomSheetDialogFragment {
        mDimAmount = dimAmount
        bottomSheetDialog?.window?.setDimAmount(mDimAmount)
        return this
    }

    fun setTransparencyBottomSheetDialog(): EdgeBottomSheetDialogFragment {
        mIsTransparencyBottomSheetDialog = true
        return this
    }

    fun show(tag: String? = null): EdgeBottomSheetDialogFragment {
        if (TextUtils.isEmpty(tag)) {
            show(fm, "${System.currentTimeMillis()}")
            return this
        } else {
            show(fm, tag)
            return this
        }
    }

    private fun setOnClick() {
        mView?.let {
            mOnClickIdRes.forEach {
                mView!!.findViewById<View>(it)?.setOnClickListener(this)
            }
        }
    }

    companion object {
        /**
         * @return 返回BottomSheetDialogFragment
         * @param fm 管理器
         * @param layoutRes 布局
         */
        fun build(fm: FragmentManager, @LayoutRes layoutRes: Int): EdgeBottomSheetDialogFragment {
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

}