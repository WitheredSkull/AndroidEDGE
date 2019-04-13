package com.daniel.edge.utils.photo

import android.graphics.Bitmap
import android.net.Uri

interface OnPhotoListener {
    fun onChooseUri(uris:Array<Uri>?)
}