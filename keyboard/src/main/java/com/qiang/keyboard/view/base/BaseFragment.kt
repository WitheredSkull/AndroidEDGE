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
import com.qiang.keyboard.viewModel.base.BaseViewModel

abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment() {
    private lateinit var mDataBinding: DB
    private lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, initLayout(), container, false)
        mDataBinding.lifecycleOwner = this
        initViewModel(mDataBinding)
        initData()
        return mDataBinding.root
    }

    fun setViewModel(clazz: Class<out VM>):VM{
        mViewModel = ViewModelProviders.of(this).get(clazz)
        return mViewModel
    }

    fun setViewModel(viewModel:VM):VM{
        mViewModel = viewModel
        return mViewModel
    }

    fun getViewModel():VM{
        return mViewModel
    }

    fun getDataBinding():DB{
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

    /**
     * 初始化ViewModel
     */
    abstract fun initViewModel(dataBinding: DB)

    /**
     * 初始化数据
     */
    abstract fun initData()
}