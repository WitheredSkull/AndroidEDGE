package com.shuanglu.edge.Management.Permission

import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.daniel.edge.Config.EdgeConfig
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

    fun requestPermission(vararg permission: String): EdgePermissionManagement {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.addAll(permission)
            //Filtrate Agree Permission
            mList.filter {
                if (ContextCompat.checkSelfPermission(EdgeConfig.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
                    false
                } else {
                    true
                }
            }
        }
        return this
    }

    fun requestPackageNeedPermission(): EdgePermissionManagement {
        var permissions =
            EdgeApplicationManagement.appPermissionFromPackageInfo(EdgeApplicationManagement.appPackageName())
                ?.requestedPermissions
        permissions?.filter {
            if (ContextCompat.checkSelfPermission(EdgeConfig.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
                false
            } else {
                true
            }
        }
        permissions?.forEach {
            mList.add(it)
        }
        return this
    }

    fun setCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack): EdgePermissionManagement {
        mOnEdgePermissionCallBack = onEdgePermissionCallBack
        return this
    }

    fun build(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                mActivity?.clear()
            }
            mActivity = WeakReference(activity)
            mActivity?.get()?.requestPermissions(mList.toTypedArray(), REQUEST_PERMISSION)
        }
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
                mOnEdgePermissionCallBack?.onRequestPermissionFailure(dangerPermissions)
            } else {
                mActivity?.clear()
                mOnEdgePermissionCallBack?.onRequestPermissionSuccess()
            }
        }
    }

}