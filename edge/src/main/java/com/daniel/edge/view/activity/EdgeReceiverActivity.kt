package com.daniel.edge.view.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.webkit.WebChromeClient
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daniel.edge.R
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.IEdgePermissionManagement
import com.daniel.edge.utils.photo.IEdgePhoto
import com.daniel.edge.utils.photo.PhotoMethod
import com.daniel.edge.utils.system.EdgeSystemUtils
import com.daniel.edge.window.dialog.IEdgeDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener
import java.io.File
import java.lang.Exception

/**
 * @see EdgeReceiverActivity 这是一个隐藏的专用activity，内置广播发送实现某些特殊需求
 */
class EdgeReceiverActivity : AppCompatActivity(), ChooseTextAdapter.OnAdapterClickListener, OnEdgeDialogClickListener {


    lateinit var mChooseStrings: Array<String>
    var mFileUri: Uri? = null
    var mTempPhoto: String? = null
    var mIsResult = false
    override fun onClick(parent: View, view: View, dialog: Dialog) {
        dialog.dismiss()
        IEdgePhoto?.onChoosePhotoUris(null)
        clear()
    }

    override fun onClick(view: View, position: Int, data: String) {
        when (position + 1) {
            PhotoMethod.GALLERY.ordinal -> openGallery()
            PhotoMethod.CAMERA.ordinal -> openCamera(mTempPhoto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止窗口的点击，让点击事件透传
        window.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )
        //初始化需要的功能
        initFunction()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mIsResult = true
        when (requestCode) {
            REQUEST_OPEN_Gallery_CODE -> resultGallery(resultCode, data)

            REQUEST_OPEN_CAMERA_CODE -> resultCamera()
            else -> {
            }
        }
        //回调完成也需要结束
        clear()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == EdgePermissionManagement.REQUEST_PERMISSION) {
            mIsResult = true
            resultPermission(permissions, grantResults)
            //不排除自己调用自己的时候会意外结束，所以加入判断，如果是获取权限时才结束权限，否则不结束
            if (EdgeActivityFunction.PERMISSION.PERMISSION.equals(intent.getStringExtra(EdgeActivityFunction.FUNCTION))) {
                clear()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    //清空资源
    open fun clear() {
        mTempPhoto = null
        mFileUri = null
        IEdgePhoto = null
        IEdgePermissionManagement = null
        finish()
    }

    /**
     * @sample initFunction 初始化需要的功能
     */
    open fun initFunction() {
        if (intent == null) {
            clear()
            return
        }
        try {
            when (intent.getStringExtra(EdgeActivityFunction.FUNCTION)) {
                EdgeActivityFunction.PERMISSION.PERMISSION -> {
                    //权限获取
                    initPermission(
                        intent.getStringArrayExtra(EdgeActivityFunction.PERMISSION.PERMISSION_PARAMETER)
                    )
                }
                EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_CHOOSE -> {
                    //获取照片
                    mTempPhoto = intent.getStringExtra(EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_PATH)
                    initPhoto(
                        PhotoMethod.values()[intent.getIntExtra(
                            EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_METHOD,
                            PhotoMethod.ALL.ordinal
                        )],
                        mTempPhoto
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun initPermission(_ps: Array<out String>?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && _ps != null) {
            requestPermissions(
                _ps,
                EdgePermissionManagement.REQUEST_PERMISSION
            )
        } else {
            onRequestPermissionsResult(EdgePermissionManagement.REQUEST_PERMISSION, arrayOf(), intArrayOf())
        }
    }

    open fun initPhoto(method: PhotoMethod, _p: String?) {
        mChooseStrings = resources.getStringArray(R.array.choose_photo)
        when (method) {
            PhotoMethod.ALL -> {
                //选择上传文件
                EdgeBottomSheetDialogFragment
                    .build(supportFragmentManager, R.layout.dialog_choose_baise)
                    .setTransparencyBottomSheetDialog()
                    .setDialogCallback(object : IEdgeDialogCallback {
                        override fun onDialogDisplay(v: View?, dialog: Dialog) {
                            val rv = v?.findViewById<RecyclerView>(R.id.rv)
                            rv?.layoutManager = LinearLayoutManager(this@EdgeReceiverActivity)
                            rv?.adapter = ChooseTextAdapter(mChooseStrings, this@EdgeReceiverActivity)
                        }

                        override fun onDialogDismiss() {
                            if (!mIsResult) {
                                IEdgePhoto?.onChoosePhotoUris(null)
                                clear()
                            }
                        }
                    })
                    .addOnClick(this@EdgeReceiverActivity, R.id.tv)
                    .show()
            }
            PhotoMethod.GALLERY -> {
                openGallery()
            }
            PhotoMethod.CAMERA -> {
                openCamera(_p)
            }
        }
    }

    /**
     * @param _p Path，照片保存的地址
     */
    open fun openCamera(_p: String?) {
        val file = File(_p)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mFileUri = Uri.fromFile(file)
        } else {
            mFileUri = FileProvider.getUriForFile(
                this,
                EdgeApplicationManagement.appPackageName() + ".EdgeFileProvider",
                file
            )
        }
        val cameraIntent = EdgeSystemUtils.getCameraIntent(mFileUri!!)
        startActivityForResult(cameraIntent, REQUEST_OPEN_CAMERA_CODE)
    }

    open fun openGallery() {
        val galleryIntent = EdgeSystemUtils.getGalleryIntent()
        startActivityForResult(galleryIntent, REQUEST_OPEN_Gallery_CODE)
    }

    /**
     * @sample resultCamera 这是WebView拍照上传回调,支持重写
     */
    fun resultCamera() {
        //有可能是没有拍照哦
        if (mFileUri != null && File(mFileUri?.encodedPath).exists()) {
            IEdgePhoto?.onChoosePhotoUris(arrayOf(mFileUri!!))
        } else {
            IEdgePhoto?.onChoosePhotoUris(null)
        }
    }

    /**
     * @sample resultGallery 这是WebView图片上传回调,支持重写
     */
    fun resultGallery(resultCode: Int, data: Intent?) {
        IEdgePhoto?.onChoosePhotoUris(WebChromeClient.FileChooserParams.parseResult(resultCode, data))
    }

    /**
     * @sample requestPermissions 权限请求结果
     */
    open fun resultPermission(
        permissions: Array<out String>?,
        grantResults: IntArray?
    ) {
        val dangerPermissions = arrayListOf<String>()
        if (permissions != null && grantResults != null) {
            if (grantResults.size > 0) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        dangerPermissions.add(permissions[i])
                    }
                }
            }
        }
        IEdgePermissionManagement?.onResult(dangerPermissions)
    }

    companion object {
        var IEdgePermissionManagement: IEdgePermissionManagement? = null
        var IEdgePhoto: IEdgePhoto? = null
        val REQUEST_OPEN_CAMERA_CODE = 3423
        val REQUEST_OPEN_Gallery_CODE = 3422
    }
}