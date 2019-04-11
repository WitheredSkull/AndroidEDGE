package com.daniel.edge.management.permission

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager


class EdgePermissionActivity : AppCompatActivity() {
    companion object {
        val PREMISSION_PARAMETER = "PREMISSION_PARAMETER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )
        var requestPermission = intent.getStringArrayExtra(PREMISSION_PARAMETER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && intent != null && requestPermission != null) {
            requestPermissions(requestPermission, EdgePermissionManagement.REQUEST_PERMISSION)
        } else {
            onRequestPermissionsResult(EdgePermissionManagement.REQUEST_PERMISSION, arrayOf(), intArrayOf())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(EdgePermissionReceiver.ACTION)
        intent.putExtra(
            EdgePermissionReceiver.FUNCTION,
            EdgePermissionReceiver.Companion.FUNCTION_KEY.onRequestPermissionsResult.name
        )
        intent.putExtra(EdgePermissionReceiver.Companion.PARAMETER_KEY.requestCode.name, requestCode)
        intent.putExtra(EdgePermissionReceiver.Companion.PARAMETER_KEY.permissions.name, permissions)
        intent.putExtra(EdgePermissionReceiver.Companion.PARAMETER_KEY.grantResults.name, grantResults)
        sendBroadcast(intent)
        //如果权限请求成功可以不需要这个activity了
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}