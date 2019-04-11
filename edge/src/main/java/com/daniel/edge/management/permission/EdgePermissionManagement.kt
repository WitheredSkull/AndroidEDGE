package com.daniel.edge.management.permission

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.log.EdgeLog

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 9:23
 * @Description:
 *
 */
class EdgePermissionManagement() : IEdgePermissionManagement {
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
        }
    }


    companion object {
        val REQUEST_PERMISSION = 999
        private var onEdgePermissionCallBack: OnEdgePermissionCallBack? = null
        private var mList = arrayListOf<String>()
        private var mReceiver: EdgePermissionReceiver? = null

        fun setOnCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack): EdgePermissionManagement.Companion {
            this.onEdgePermissionCallBack = onEdgePermissionCallBack
            return this
        }

        /**
         * @param permission 请求Manifest.permission中选中的权限，不支持Manifest.group
         */
        fun requestPermission(vararg permission: String): EdgePermissionManagement.Companion {
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

        /**
         * 请求所有的权限
         */
        fun requestPackageNeedPermission(): EdgePermissionManagement.Companion {
            var permissions = EdgePermissionUtils.getPackageNeedPermission()
            permissions?.forEach {
                mList.add(it)
            }
            return this
        }

        /**
         * @return 如果版本大于6.0则申请权限，版本小于6.0直接回调成功
         */
        fun build(): EdgePermissionManagement {
            val instance = EdgePermissionManagement()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mList.isNotEmpty()) {
                //打开隐藏的Activity去申请权限
                mReceiver = EdgePermissionReceiver(instance)
                val intentReceiver = IntentFilter(EdgePermissionReceiver.ACTION)
                Edge.CONTEXT.registerReceiver(mReceiver, intentReceiver)
                val activityIntent = Intent(Edge.CONTEXT, EdgePermissionActivity::class.java)
                activityIntent.putExtra(EdgePermissionActivity.PREMISSION_PARAMETER, mList.toTypedArray())
                Edge.CONTEXT.startActivity(activityIntent)
            } else {
                instance.onRequestPermissionsResult(REQUEST_PERMISSION, null, null)
            }
            return instance
        }
    }

}