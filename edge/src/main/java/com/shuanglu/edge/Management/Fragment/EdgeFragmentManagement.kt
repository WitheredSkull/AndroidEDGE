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
    var mFragments:ArrayList<Fragment> = arrayListOf()
    var mFragmentManager:FragmentManager
    private var mPosition = -1

    constructor(fragmentManager: FragmentManager){
        mFragmentManager = fragmentManager
    }

    fun build(@IdRes id:Int){
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.add(id,it)
            ft.hide(it)
        }
        ft.commit()
    }

    fun show(index:Int):EdgeFragmentManagement{
        var ft = mFragmentManager.beginTransaction()
        if (mPosition != -1){
            ft.hide(mFragments[mPosition])
        }
        ft.show(mFragments[index]).commit()
        mPosition = index
        return this
    }

    fun hideAll():EdgeFragmentManagement{
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.hide(it)
        }
        ft.commit()
        return this
    }

    fun destroy(){
        var ft = mFragmentManager.beginTransaction()
        mFragments.forEach {
            ft.remove(it)
        }
        ft.commitAllowingStateLoss()
        mFragments.clear()
    }
}