package com.qiang.keyboard.view.base

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.qiang.keyboard.BR
import com.qiang.keyboard.R
import com.qiang.keyboard.expand.createOptionsMenu
import com.qiang.keyboard.expand.optionsItemSelected
import com.qiang.keyboard.presenter.BasePresenter
import com.qiang.keyboard.viewModel.base.BaseViewModel
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 * @author Daniel
 * @time 2019/5/9
 * @param DB ViewDataBinding
 * @param VM ViewModel 需要继承BaseViewModel
 *
 * 使用BaseVMActivity必须修改setContentView(layout)为setContentView(layout,true or false)
 *
 * 需要修改ViewModel的初始化方式请重写initViewModel()方法
 */
abstract class BaseVMActivity<DB : ViewDataBinding, VM : BaseViewModel?> : AppCompatActivity() {
    //abstract class BaseVMActivity<C : BaseCallback, P : BasePresenter<C>> : AppCompatActivity() {
//    private var mPresenter: P? = null
    private lateinit var mDataBinding: DB
    private var mViewModel: VM? = null
    private var isEnableMenu = false

//        mPresenter = initPresenter()
//        mPresenter?.setCallback(initCallback())

    /**
     * 主要内容：初始化DataBinding,初始化ViewModel，初始化数据，初始化监听
     */
    fun setContentView(layoutResID: Int, isUseViewModel: Boolean) {
        mDataBinding = DataBindingUtil.setContentView(this, layoutResID)
        if (isUseViewModel) {
            mViewModel = initViewModel()
            mDataBinding.setVariable(BR.viewModel, mViewModel)
            mDataBinding.executePendingBindings()
            mDataBinding.lifecycleOwner = this
        }
        initData()
        initListener()
    }

    fun enableMenu() {
        isEnableMenu = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
        if (isEnableMenu)
            createOptionsMenu(menu, true)
        else
            super.onCreateOptionsMenu(menu)


    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (optionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)

//    fun getPresenter() = mPresenter

    override fun onDestroy() {
        super.onDestroy()
//        mPresenter?.onDestroy()
    }

    fun getViewModel(): VM? {
        return mViewModel
    }

    fun getDataBinding(): DB {
        return mDataBinding
    }

    abstract fun initData()
    abstract fun initListener()
    /**
     * 初始化Presenter层
     */
//    abstract fun initPresenter(): P?

    /**
     * 初始化View Callback
     */
//    abstract fun initCallback():C?

    open fun initViewModel(): VM? {
        val clazz = getTClass<VM>(1)
        if (clazz != null) {
            return ViewModelProviders.of(this@BaseVMActivity).get(clazz)
        } else {
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