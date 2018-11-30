package com.shuanglu.edge.Management.Fragment

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.daniel.edge.Utils.Log.EdgeLog

/**
 * @Author:      Daniel
 * @Date:        2018/11/22 14:56
 * @Description:Fragment管理器，仅用于一个页面
 *
 */
class EdgeFragmentManagement {
    var mFragments: ArrayList<Fragment> = arrayListOf()
    var mFragmentManager: FragmentManager
    private var mPosition = -1

    constructor(fragmentManager: FragmentManager) {
        mFragmentManager = fragmentManager
    }

    //传入Fragment类可以自动判断FragmentManager是否已经存在，存在则复用，不存在则重新创建
    fun introductionFragment(vararg clazz: Class<*>) {
        clazz.forEach {
            var fragment = mFragmentManager.findFragmentByTag(clazz.javaClass.simpleName)
            if (fragment != null) {
                mFragments.add(fragment)
            } else {
                mFragments.add(it.newInstance() as Fragment)
            }
        }
        var sortFlag = true
        mFragments.forEachIndexed { index, fragment ->
            if (!fragment.javaClass.simpleName.equals(
                    if (mFragmentManager?.fragments.size > index) {
                        mFragmentManager?.fragments[index]
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

    }

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

    fun show(index: Int): EdgeFragmentManagement {
        var ft = mFragmentManager.beginTransaction()
        if (mPosition != -1) {
            ft.hide(mFragments[mPosition])
        }
        ft.show(mFragments[index]).commit()
        mPosition = index
        return this
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

    //移除管理器中所有的Fragment，防止发生显示混乱等情况
    fun removeAll(): EdgeFragmentManagement {
        if (mFragmentManager.fragments.size > 0) {
            var sfm = mFragmentManager.beginTransaction()
            mFragmentManager.fragments.forEach() {
                sfm.remove(it)
            }
            sfm.commit()
        }
        return this
    }

    fun destroy() {
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.remove(it)
        }
        ft.commitAllowingStateLoss()
        mFragments.clear()
    }
}