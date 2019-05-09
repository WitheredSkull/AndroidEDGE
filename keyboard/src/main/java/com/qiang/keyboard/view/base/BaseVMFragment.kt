package com.qiang.keyboard.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.qiang.keyboard.BR
import com.qiang.keyboard.viewModel.base.BaseViewModel
import java.lang.Exception
import java.lang.reflect.ParameterizedType
/**
 * @author Daniel
 * @time 2019/5/9
 * @param DB ViewDataBinding
 * @param VM ViewModel 需要继承BaseViewModel
 *
 * 需要修改ViewModel的初始化方式请重写initViewModel()方法
 */
abstract class BaseVMFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {
    private lateinit var mDataBinding: DB
    private var mViewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, initLayout(), container, false)
        if (enableViewModel()) {
            mViewModel = initViewModel()
            getDataBinding().setVariable(BR.viewModel, mViewModel)
            getDataBinding().executePendingBindings()
            getDataBinding().lifecycleOwner = this
        }
        initData()
        initListener()
        return mDataBinding.root
    }

    fun getViewModel(): VM? {
        return mViewModel
    }

    fun getDataBinding(): DB {
        return mDataBinding
    }

    override fun onDestroyView() {
        mDataBinding.unbind()
        super.onDestroyView()
    }

    /**
     * 初始化布局
     */
    @LayoutRes
    abstract fun initLayout(): Int

    abstract fun enableViewModel(): Boolean

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化监听器
     */
    abstract fun initListener()

    open fun initViewModel():VM? {
        val clazz = getTClass<VM>(1)
        if (clazz != null) {
            return ViewModelProviders.of(this@BaseVMFragment).get(clazz)
        }else{
            return null
        }
    }

    /**
     * 通过反射获取类
     */
    fun <T> getTClass(position: Int): Class<T>? {
        return try {
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[position] as Class<T>
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}