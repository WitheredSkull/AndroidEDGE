package com.daniel.edge.utils.photo

import android.content.Intent
import android.net.Uri
import com.daniel.edge.config.Edge
import com.daniel.edge.view.activity.EdgeActivityFunction
import com.daniel.edge.view.activity.EdgeReceiverActivity

/**
 * 完整调用
EdgePhotoUtils()
.setOnPhotoListener(this)
.setPhotoPath("${EdgeFileManagement.getEdgeExternalDCIMPath()}/${System.currentTimeMillis()}.jpg")
.setChooseMethod(PhotoMethod.ALL)
.build()
 */
class EdgePhotoUtils : IEdgePhoto {

    private var mOnPhotoListener: OnPhotoListener? = null
    private var mPath: String? = null
    private var mPhotoMethod = PhotoMethod.ALL
    override fun onChoosePhotoUris(_u: Array<Uri>?) {
        mOnPhotoListener?.onChooseUri(_u)
    }

    /**
     * @sample build 打开一个activity用于承载选择图库的工作
     */
    fun build(): EdgePhotoUtils {
        if (mPath.isNullOrEmpty()) {
            throw Exception("Please use setPhotoPath")
        } else {
            EdgeReceiverActivity.IEdgePhoto = this
            val intent = Intent(Edge.CONTEXT, EdgeReceiverActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(EdgeActivityFunction.FUNCTION, EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_CHOOSE)
            intent.putExtra(EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_PATH, mPath)
            intent.putExtra(EdgeActivityFunction.PHOTO_CHOOSE.PHOTO_METHOD, mPhotoMethod.ordinal)
            Edge.CONTEXT.startActivity(intent)
        }
        return this
    }

    /**
     * @param _m 选择打开图片的方式
     */
    fun setChooseMethod(_m: PhotoMethod): EdgePhotoUtils {
        mPhotoMethod = _m
        return this
    }

    /**
     * @param _l 设置图片选择回调监听
     *
     */
    fun setOnPhotoListener(_l: OnPhotoListener): EdgePhotoUtils {
        mOnPhotoListener = _l
        return this
    }

    /**
     * @param _p 相机需要传入路径
     */
    fun setPhotoPath(_p: String): EdgePhotoUtils {
        this.mPath = _p
        return this
    }
}