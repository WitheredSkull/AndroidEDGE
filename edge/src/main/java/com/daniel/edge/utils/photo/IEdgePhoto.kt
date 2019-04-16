package com.daniel.edge.utils.photo

import android.net.Uri

interface IEdgePhoto {
    /**
     * 选择图片完成后进行回调
     */
    fun onChoosePhotoUris(_u:Array<Uri>?)
}