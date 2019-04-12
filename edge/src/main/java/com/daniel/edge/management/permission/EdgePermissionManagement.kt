package com.daniel.edge.management.permission

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.daniel.edge.config.Edge
import com.daniel.edge.recriver.EdgeReceiveFunction
import com.daniel.edge.recriver.EdgeReceiver
import com.daniel.edge.utils.permission.EdgePermissionUtils
import com.daniel.edge.view.activity.EdgeActivityFunction
import com.daniel.edge.view.activity.EdgeReceiverActivity

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 9:23
 * @Description:
 *
 */
class EdgePermissionManagement() : IEdgePermissionManagement {


    private var mList = arrayListOf<String>()
    private var mReceiver: EdgeReceiver? = null
    private var onEdgePermissionCallBack: OnEdgePermissionCallBack? = null
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>?,
        grantResults: IntArray?
    ) {
        if (requestCode == REQUEST_PERMISSION && permissions != null && grantResults != null) {
            if (grantResults.size > 0) {
                val dangerPermissions = arrayListOf<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        dangerPermissions.add(permissions[i])
                    }
                }
                if (dangerPermissions.size > 0) {
                    onEdgePermissionCallBack?.onRequestPermissionFailure(dangerPermissions)
                } else {
                    onEdgePermissionCallBack?.onRequestPermissionSuccess()
                }
            }
        } else {
            onEdgePermissionCallBack?.onRequestPermissionSuccess()
        }
        mReceiver?.let {
            Edge.CONTEXT.unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }

    override fun setOnCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack): EdgePermissionManagement {
        this.onEdgePermissionCallBack = onEdgePermissionCallBack
        return this
    }

    /**
     * @return 如果版本大于6.0则申请权限，版本小于6.0直接回调成功
     */
    override fun build() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mList.isNotEmpty()) {
            //打开隐藏的Activity去申请权限
            mReceiver = EdgeReceiver(this)
            val intentReceiver = IntentFilter(EdgeReceiveFunction.Permissions.ACTION_PERMISSION)
            Edge.CONTEXT.registerReceiver(mReceiver, intentReceiver)
            val activityIntent = Intent(Edge.CONTEXT, EdgeReceiverActivity::class.java)
            activityIntent.putExtra(EdgeActivityFunction.FUNCTION, EdgeActivityFunction.PERMISSION.PERMISSION)
            activityIntent.putExtra(EdgeActivityFunction.PERMISSION.PERMISSION_PARAMETER, mList.toTypedArray())
            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            Edge.CONTEXT.startActivity(activityIntent)
        } else {
            onRequestPermissionsResult(REQUEST_PERMISSION, null, null)
        }
    }

    /**
     * 请求所有的权限
     */
    override fun requestPackageNeedPermission(): EdgePermissionManagement {
        mList.clear()
        var permissions = EdgePermissionUtils.getPackageNeedPermission()
        permissions?.forEach {
            mList.add(it)
        }
        return this
    }

    /**
     * @param permission 请求Manifest.permission中选中的权限，不支持Manifest.group
     */
    override fun requestPermission(vararg permission: String): EdgePermissionManagement {
        mList.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mList.addAll(permission)
            //Filtrate Agree Permission
            var listFilter = mList.filter {
                if (ContextCompat.checkSelfPermission(Edge.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
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

    companion object {
        val REQUEST_PERMISSION = 999
        fun Build() = EdgePermissionManagement()
    }

}