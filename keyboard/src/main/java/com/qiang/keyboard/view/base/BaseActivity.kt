package com.qiang.keyboard.view.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.qiang.keyboard.presenter.BasePresenter
import com.qiang.keyboard.viewModel.base.BaseViewModel

abstract class BaseActivity<DB : ViewDataBinding,VM:BaseViewModel> : AppCompatActivity() {
    //abstract class BaseActivity<C : BaseCallback, P : BasePresenter<C>> : AppCompatActivity() {
//    private var mPresenter: P? = null
    private var mDataBinding: DB? = null
    private var mViewModel:VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = initDataBinding()
//        mDataBinding?.lifecycleOwner = this
        if (mDataBinding != null)
        initViewModel(mDataBinding!!)
        initData()
//        mPresenter = initPresenter()
//        mPresenter?.setCallback(initCallback())
    }

//    fun getPresenter() = mPresenter

    override fun onDestroy() {
        super.onDestroy()
//        mPresenter?.onDestroy()
    }

    fun setViewModel(clazz: Class<out VM>):VM{
        mViewModel = ViewModelProviders.of(this).get(clazz)
        return mViewModel!!
    }

    fun setViewModel(viewModel:VM){
        mViewModel = viewModel
    }

    fun getViewModel():VM?{
        return mViewModel
    }

    fun getDataBinding():DB?{
        return mDataBinding
    }

    abstract fun initDataBinding(): DB?
    abstract fun initViewModel(dataBinding: DB)
    abstract fun initData()
    /**
     * 初始化Presenter层
     */
//    abstract fun initPresenter(): P?

    /**
     * 初始化View Callback
     */
//    abstract fun initCallback():C?
}