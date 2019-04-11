package com.daniel.edge.management.permission

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class EdgePermissionReceiver : BroadcastReceiver {


    var iEdgePermissionManagement: IEdgePermissionManagement? = null
    override fun onReceive(context: Context, intent: Intent) {
        intent?.let {
            if (it.action.equals(ACTION)) {
                when (it.getStringExtra(FUNCTION)) {
                    //回调权限请求结果
                    FUNCTION_KEY.onRequestPermissionsResult.name -> {
                        iEdgePermissionManagement?.onRequestPermissionsResult(
                            intent.getIntExtra(PARAMETER_KEY.requestCode.name, 0),
                            intent.getStringArrayExtra(PARAMETER_KEY.permissions.name),
                            intent.getIntArrayExtra(PARAMETER_KEY.grantResults.name)
                        )
                    }
                }
            }
        }
    }

    companion object {
        val ACTION = "PermissionBroadcast"
        val FUNCTION = "FUNCTION"

        enum class FUNCTION_KEY {
            onRequestPermissionsResult
        }

        enum class PARAMETER_KEY {
            requestCode, permissions, grantResults
        }
    }

    constructor(iEdgePermissionManagement: IEdgePermissionManagement) : super() {
        this.iEdgePermissionManagement = iEdgePermissionManagement
    }

    constructor() : super()

}
