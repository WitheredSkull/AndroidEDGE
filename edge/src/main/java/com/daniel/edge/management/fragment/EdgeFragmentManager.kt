package com.daniel.edge.management.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.daniel.edge.utils.clazz.EdgeClassUtils

object EdgeFragmentManager {
    private const val TAG = "FragmentManager"

    /**添加Fragment并且默认显示第一个
     * @param idRes 对应的布局ID
     * @param fm
     * @param clazz 对应的Fragment的Class
     */
    fun addFragments(@IdRes idRes: Int, fm: FragmentManager, vararg clazz: Class<*>) {
        var begin = fm.beginTransaction()
        var firstFragment: Fragment? = null
        clazz.forEachIndexed { index, aClazz ->
            var fragment = fm.findFragmentByTag(EdgeClassUtils.classToText(aClazz, TAG))
            if (fragment == null) {
                fragment = aClazz.newInstance() as Fragment
                begin.add(idRes, fragment, EdgeClassUtils.classToText(aClazz, TAG)).hide(fragment)
            }
            if (index == 0) {
                firstFragment = fragment!!
            }
        }
        if (firstFragment != null) {
            begin.show(firstFragment!!)
        }
        begin.commit()
    }

    /**重新加载Fragment
     * @param layout 对应的布局ID
     * @param fm
     * @param clazz 对应的Fragment的Class
     */
    fun reloadFragment(@IdRes idRes: Int, fm: FragmentManager, clazz: Class<*>) {
        var fragment = fm.findFragmentByTag(EdgeClassUtils.classToText(clazz, TAG))
        var begin = fm.beginTransaction()
        if (fragment != null) {
            begin.remove(fragment).commit()
        }
        hideAll(fm)
        begin = fm.beginTransaction()
        fragment = clazz.newInstance() as Fragment
        begin.add(idRes, fragment, EdgeClassUtils.classToText(clazz, TAG))
        begin.show(fragment)
    }

    /**切换Fragment
     * @param fm
     * @param clazz 对应的Fragment的Class
     */
    fun switchFragment(@IdRes idRes: Int, fm: FragmentManager, clazz: Class<*>) {
        hideAll(fm)
        var fragment = fm.findFragmentByTag(EdgeClassUtils.classToText(clazz, TAG))
        if (fragment != null) {
            fm.beginTransaction().show(fragment).commit()
        } else {
            addFragments(idRes, fm, clazz)
        }
    }

    /**
     * 切换Fragment
     * @param fm 隐藏所有的Fragment
     */
    fun hideAll(fm: FragmentManager) {
        var begin = fm.beginTransaction()
        fm.fragments.forEach {
            begin.hide(it)
        }
        begin.commit()
    }
}