package com.daniel.edge.Utils.Safety

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
    /**
     * @param s 需要加密的字符串
     * @return 加密后的字符串
     */
    fun encrypt(s: String): String {
        return String(Base64.encode(s.toByteArray(), Base64.DEFAULT))
    }

    /**
     * @param s64 需要解密的字符串
     * @return 解密后的字符串
     */
    fun decode(s64: String): String {
        return String(Base64.decode(s64.toByteArray(), Base64.DEFAULT))
    }

    /**
     * @param bitmap 需要加密的图片
     * @return 加密后的图片Base64字符串
     */
    fun bitmapToBase64(bitmap: Bitmap): String {
        var byteStream: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byteStream.flush()
        byteStream.close()
        return String(Base64.encode(byteStream.toByteArray(), Base64.DEFAULT))
    }

    /**
     * @param s 需要解密的字符串
     * @return 解密后的图片
     */
    fun base64toBitmap(s64: String): Bitmap {
        var byteString: ByteArray = Base64.decode(s64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteString, 0, byteString.size)
    }
}