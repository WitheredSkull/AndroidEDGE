package com.daniel.edge.utils.safety

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * 创建人 Daniel
 * 时间   2018/11/5.
 * 简介   xxx
 */
object EdgeBase64Utils {
    fun encrypt(s: String): String {
        return String(Base64.encode(s.toByteArray(), Base64.DEFAULT))
    }

    fun decode(s64: String): String {
        return String(Base64.decode(s64.toByteArray(), Base64.DEFAULT))
    }

    fun bitmap2Base64(bitmap: Bitmap): String {
        var byteStream: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byteStream.flush()
        byteStream.close()
        return String(Base64.encode(byteStream.toByteArray(), Base64.DEFAULT))
    }

    fun base642Bitmap(s64: String): Bitmap {
        var byteString: ByteArray = Base64.decode(s64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteString, 0, byteString.size)
    }
}