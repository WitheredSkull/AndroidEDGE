package com.daniel.edge.management.download

import com.daniel.edge.config.Edge
import com.daniel.edge.management.download.model.FileDownload
import com.daniel.edge.utils.log.EdgeLog
import java.io.BufferedInputStream
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

class DownloadThread(
    private var downloader: FileDownload,
    private var downloadURL: String,
    private var file: File,
    var startPosition: Long,
    var endPosition: Long
) : Thread() {


    var downLength = 0L//文件总大小
    var downloadThreadId = 0L
    var finish = false
    override fun run() {
        if (startPosition < endPosition) {//下载未完成
            try {
                var httpURLConnection = URL(downloadURL).openConnection() as HttpURLConnection
                httpURLConnection.connectTimeout =
                    Edge.CONNECT_TIMEOUT
                httpURLConnection.readTimeout =
                    Edge.READ_TIMEOUT
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"
                )
                httpURLConnection.setRequestProperty("Accept-Language", "zh-CN")
                httpURLConnection.setRequestProperty("Referer", downloadURL.toString())
                httpURLConnection.setRequestProperty("Charset", "UTF-8")
                httpURLConnection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"
                )
                httpURLConnection.setRequestProperty("Range", "bytes=${startPosition}-${endPosition}")
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive")
                val inStream = BufferedInputStream(httpURLConnection.inputStream)
                val buffer = ByteArray(10 * 1024)
                var offset = 0
                val threadFile = RandomAccessFile(file, "rwd")
                threadFile.seek(startPosition)
                offset = inStream.read(buffer)
                while (offset != -1) {
//                    EdgeLog.show(javaClass, "下载", "${offset} == ${inStream.available()}")
                    threadFile.write(buffer, 0, offset)
                    downLength += offset
                    startPosition += offset
                    downloader.updateLoadSize(downloadThreadId, startPosition,offset.toLong())//需要加入Update最新数据的操作
                    offset = inStream.read(buffer)
                }
                threadFile.close()
                inStream.close()
                finish = true
            } catch (e: Exception) {
                e.printStackTrace()
                EdgeLog.e(javaClass, "下载发生异常", e.cause?.message)
            } finally {
                EdgeLog.e(javaClass, "下载完成", "SUCCESS")
                downloader.onThreadExit(downloadThreadId)
            }
        }
//        super.run()
    }

    fun isFinish(): Boolean {
        return finish
    }

    init {
        downLength = endPosition - startPosition
    }
}