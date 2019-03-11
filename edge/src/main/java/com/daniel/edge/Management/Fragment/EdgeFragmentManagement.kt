package com.dongtinghu.shellbay.View.Activity.Home

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.daniel.edge.Utils.Log.EdgeLog

/**
 * @Author:      Daniel
 * @Date:        2018/11/22 14:56
 * @Description:Fragment管理器，仅用于一个页面
 *
 */
class EdgeFragmentManagement {

    var mFragmentManager: androidx.fragment.app.FragmentManager
    var mFragments: ArrayList<androidx.fragment.app.Fragment> = arrayListOf()
    private var mPosition = -1
    fun build(@IdRes id: Int) {
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            if (null != mFragmentManager.findFragmentByTag(it.javaClass.simpleName)) {
                EdgeLog.show(javaClass, "recover ${it.javaClass.simpleName}")
            } else {
                ft.add(id, it, it.javaClass.simpleName)
                ft.hide(it)
            }
        }
        ft.commit()
    }

    fun destroy() {
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.remove(it)
        }
        ft.commitAllowingStateLoss()
        mFragments.clear()
    }

    //获取正在显示的
    fun getPosition(): Int {
        return mPosition
    }

    //隐藏所有Fragment
    fun hideAll(): EdgeFragmentManagement {
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.hide(it)
        }
        ft.commit()
        return this
    }

    //传入Fragment类可以自动判断FragmentManager是否已经存在，存在则复用，不存在则重新创建
    fun introductionFragment(vararg clazz: Class<*>): EdgeFragmentManagement {
        clazz.forEach {
            var fragment = mFragmentManager.findFragmentByTag(clazz.javaClass.simpleName)
            if (fragment != null) {
                mFragments.add(fragment)
            } else {
                mFragments.add(it.newInstance() as androidx.fragment.app.Fragment)
            }
        }
        var sortFlag = true
        mFragments.forEachIndexed { index, fragment ->
            if (!fragment.javaClass.simpleName.equals(
                    if (mFragmentManager?.fragments.size > index) {
                        mFragmentManager?.fragments[index].javaClass.simpleName
                    } else {
                        ""
                    }
                )
            ) {
                sortFlag = false
            }
        }
        if (!sortFlag) {
            removeAll()
        }
        return this
    }

    //移除管理器中所有的Fragment，防止发生显示混乱等情况
    fun removeAll(): EdgeFragmentManagement {
        if (mFragmentManager.fragments.size > 0) {
            mFragmentManager.fragments.forEach() {
                var sfm = mFragmentManager.beginTransaction()
                sfm.remove(it)
                sfm.commit()
            }
        }
        return this
    }

    fun show(index: Int): EdgeFragmentManagement {
        var ft = mFragmentManager.beginTransaction()
        if (mPosition != -1) {
            ft.hide(mFragments[mPosition])
        }
        ft.show(mFragments[index]).commit()
        mPosition = index
        return this
    }

    fun findFragment(clazz: Class<*>, tag: Any): androidx.fragment.app.Fragment? {
        return mFragmentManager.findFragmentByTag("${clazz.simpleName}+${tag.hashCode()}")
    }

    constructor(fragmentManager: androidx.fragment.app.FragmentManager) {
        mFragmentManager = fragmentManager
    }
}