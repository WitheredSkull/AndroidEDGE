package com.shuanglu.edge.Management.Permission

import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.daniel.edge.Config.EdgeConfig
import com.daniel.edge.Utils.Log.EdgeLog
import com.shuanglu.edge.Management.Application.EdgeApplicationManagement
import java.lang.ref.WeakReference

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 9:23
 * @Description:
 *
 */
class EdgePermissionManagement() {
    val REQUEST_PERMISSION = 999
    var mList = arrayListOf<String>()
    private var mActivity: WeakReference<AppCompatActivity>? = null
    var mOnEdgePermissionCallBack: OnEdgePermissionCallBack? = null

    companion object {
        //过滤出来所有需要的权限
        fun getNeedPermission(permission: ArrayList<String>): List<String>? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Filtrate Agree Permission
                var list = permission.filter {
                    ContextCompat.checkSelfPermission(
                        EdgeConfig.CONTEXT,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }
//                for ((i,value) in permission.withIndex()){
//                    if (ContextCompat.checkSelfPermission(
//                            EdgeConfig.CONTEXT,
//                            value
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        permission.removeAt(i)
//                    }
//                }
                return list
            }
            return null
        }

        //判断权限是否获取了
        fun isAgree(permission: ArrayList<String>): Boolean {

            var list = getNeedPermission(permission)
            if (list == null || list.size == 0) {
                return true
            } else {
                return false
            }
        }

        //获取包名所需权限
        fun getPackageNeedPermission(): ArrayList<String>? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            } else {
                return null
            }
            var permissions =
                EdgeApplicationManagement.appPermissionFromPackageInfo(EdgeApplicationManagement.appPackageName())
                    ?.requestedPermissions
            var listFilter = permissions?.filter {
                if (ContextCompat.checkSelfPermission(EdgeConfig.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
                    false
                } else {
                    true
                }
            }
            var permissionList = arrayListOf<String>()
            permissionList.addAll(listFilter!!)
            return permissionList
        }
    }

    fun requestPermission(vararg permission: String): EdgePermissionManagement {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.addAll(permission)
            //Filtrate Agree Permission
            var listFilter = mList.filter {
                if (ContextCompat.checkSelfPermission(EdgeConfig.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
                    false
                } else {
                    true
                }
            }
            mList.clear()
            mList.addAll(listFilter)
        }
        return this
    }

    fun requestPackageNeedPermission(): EdgePermissionManagement {
        var permissions = getPackageNeedPermission()
        permissions?.forEach {
            mList.add(it)
        }
        return this
    }

    fun setCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack): EdgePermissionManagement {
        mOnEdgePermissionCallBack = onEdgePermissionCallBack
        return this
    }

    fun build(activity: AppCompatActivity): EdgePermissionManagement {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                mActivity?.clear()
            }
            mActivity = WeakReference(activity)
            mActivity?.get()?.requestPermissions(mList.toTypedArray(), REQUEST_PERMISSION)
        }
        return this
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            mList.clear()
            if (grantResults.size > 0) {
                val dangerPermissions = arrayListOf<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        dangerPermissions.add(permissions[i])
                    }
                }
                mActivity?.clear()
                if (dangerPermissions.size > 0) {
                    mOnEdgePermissionCallBack?.onRequestPermissionFailure(dangerPermissions)
                } else {
                    mOnEdgePermissionCallBack?.onRequestPermissionSuccess()
                }
            } else {
                mActivity?.clear()
                mOnEdgePermissionCallBack?.onRequestPermissionSuccess()
            }
        }
    }

}