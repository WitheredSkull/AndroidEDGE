package com.daniel.edge.recriver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.daniel.edge.management.permission.IEdgePermissionManagement
import com.daniel.edge.view.webView.model.IEdgeWebChromeClient


class EdgeReceiver : BroadcastReceiver {


    var iEdgePermissionManagement: IEdgePermissionManagement? = null
    var iEdgeWebChromeClient: IEdgeWebChromeClient? = null
    override fun onReceive(context: Context, intent: Intent?) {
        intent?.let {
            when (it.action) {
                EdgeReceiveFunction.Permissions.ACTION_PERMISSION -> {
                    //回调权限请求结果
                    iEdgePermissionManagement?.onRequestPermissionsResult(
                        intent.getIntExtra(EdgeReceiveFunction.Permissions.requestCode, 0),
                        intent.getStringArrayExtra(EdgeReceiveFunction.Permissions.permissions),
                        intent.getIntArrayExtra(EdgeReceiveFunction.Permissions.grantResults)
                    )
                }
                EdgeReceiveFunction.FILE_CHOOSER.ACTION_FILE_CHOOSER -> {
                    //选择文件成功，传给WebChrome
//                    iEdgeWebChromeClient?.onOpenFileResult(
//                        intent.getIntExtra(EdgeReceiveFunction.FILE_CHOOSER.FILE_RESULT_CODE, 0),
//                        intent.getParcelableExtra(EdgeReceiveFunction.FILE_CHOOSER.FILE_INTENT) as Array<Uri>
//                    )
                }
                else -> {

                }
            }
        }
    }

    constructor(iEdgePermissionManagement: IEdgePermissionManagement) : super() {
        this.iEdgePermissionManagement = iEdgePermissionManagement
    }


    constructor() : super()
    constructor(iEdgeWebChromeClient: IEdgeWebChromeClient?) : super() {
        this.iEdgeWebChromeClient = iEdgeWebChromeClient
    }

}
