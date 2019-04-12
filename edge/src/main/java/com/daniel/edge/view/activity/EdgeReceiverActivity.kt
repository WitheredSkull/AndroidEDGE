package com.daniel.edge.view.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.webkit.WebChromeClient
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.IEdgePermissionManagement
import com.daniel.edge.recriver.EdgeReceiveFunction
import com.daniel.edge.view.webView.model.EdgeWebChromeClient
import com.daniel.edge.view.webView.model.IEdgeWebChromeClient

/**
 * @see EdgeReceiverActivity 这是一个隐藏的专用activity，内置广播发送实现某些特殊需求
 */
class EdgeReceiverActivity : AppCompatActivity() {
    var mTempPhoto: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )
        intent?.let {
            when (it.getStringExtra(EdgeActivityFunction.FUNCTION)) {
                EdgeActivityFunction.PERMISSION.PERMISSION -> {
                    //权限获取
                    var requestPermission =
                        intent.getStringArrayExtra(EdgeActivityFunction.PERMISSION.PERMISSION_PARAMETER)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestPermission != null) {
                        requestPermissions(
                            requestPermission,
                            EdgePermissionManagement.REQUEST_PERMISSION
                        )
                    } else {
                        onRequestPermissionsResult(EdgePermissionManagement.REQUEST_PERMISSION, arrayOf(), intArrayOf())
                    }
                }
                EdgeActivityFunction.FILE_CHOOSE.FILE_CHOOSE -> {
                    //WebView文件选择
                    //相册
//                    val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                    i.addCategory(Intent.CATEGORY_OPENABLE)
//                    i.type = "image/*"
//                    startActivityForResult(
//                        Intent.createChooser(i, "Image Chooser"),
//                        EdgeWebChromeClient.REQUEST_OPEN_FILE_CODE
//                    )
                    //相机
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    mTempPhoto =
                        EdgeFileManagement.getExternalStorageDirectory() + "/DCIM/" + "${System.currentTimeMillis()}.jpg"
                    cameraIntent.addCategory(Intent.CATEGORY_DEFAULT)
                    startActivityForResult(cameraIntent, EdgeWebChromeClient.REQUEST_OPEN_CAMERA_CODE)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            when (requestCode) {
                EdgeWebChromeClient.REQUEST_OPEN_FILE_CODE -> {
                    //这是WebView图片上传回调,支持重写
//                    var intent = Intent(EdgeReceiveFunction.FILE_CHOOSER.ACTION_FILE_CHOOSER)
//                    sendBroadcast(intent)
                    iEdgeWebChromeClient?.onOpenFileResult(resultCode, data)
                    iEdgeWebChromeClient = null
//                    var intent2 = Intent()
//                    intent2.action = EdgeReceiveFunction.FILE_CHOOSER.ACTION_FILE_CHOOSER
//                    intent2.putExtra(EdgeReceiveFunction.FILE_CHOOSER.FILE_RESULT_CODE, resultCode)
//                    intent2.putExtra(EdgeReceiveFunction.FILE_CHOOSER.FILE_INTENT, WebChromeClient.FileChooserParams.parseResult(resultCode, data))
//                    sendBroadcast(intent2)
                }
                EdgeWebChromeClient.REQUEST_OPEN_CAMERA_CODE -> {
                    var bitmap = BitmapFactory.decodeFile(data.data.encodedPath)
                    var bitpa2 = bitmap
                }
                else -> {

                }
            }
        }
        //回调完成也需要结束
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(EdgeReceiveFunction.Permissions.ACTION_PERMISSION)
        intent.putExtra(EdgeReceiveFunction.Permissions.requestCode, requestCode)
        intent.putExtra(EdgeReceiveFunction.Permissions.permissions, permissions)
        intent.putExtra(EdgeReceiveFunction.Permissions.grantResults, grantResults)
        sendBroadcast(intent)
        //如果权限请求成功可以不需要这个activity了
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    companion object {
        var iEdgePermissionManagement: IEdgePermissionManagement? = null
        var iEdgeWebChromeClient: IEdgeWebChromeClient? = null
    }
}