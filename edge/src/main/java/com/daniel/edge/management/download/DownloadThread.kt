package com.daniel.edge.management.download

import com.daniel.edge.config.Edge
import com.daniel.edge.management.download.model.FileDownload
import com.daniel.edge.utils.log.EdgeLog
import java.io.File
import java.io.RandomAccessFile
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class DownloadThread : Thread {
    var downloadThreadId = 0
    var downloader: FileDownload
    var downloadURL: String
    var file: File
    var threadId = -1
    var startPosition = 0
    var endPosition = 0
    var downLength = 0//文件总大小
    var finish = false

    constructor(
        downloader: FileDownload,
        downloadURL: String,
        file: File,
        startPosition: Int,
        endPosition: Int
    ) : super() {
        this.downloader = downloader
        this.downloadURL = downloadURL
        this.file = file
        this.threadId = threadId
        this.startPosition = startPosition
        this.endPosition = endPosition
        downLength = endPosition - startPosition
    }


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
                var inStream = httpURLConnection.inputStream
                var buffer = ByteArray(1024)
                var offset = 0
                EdgeLog.show(javaClass, "下载", "下载的位置${startPosition}-${endPosition}")
                var threadFile = RandomAccessFile(file, "rwd")
                threadFile.seek(startPosition.toLong())
                offset = inStream.read(buffer, 0, 1024)
                while (!downloader.isExited && (offset != -1)) {
                    threadFile.write(buffer, 0, offset)
                    downLength += offset
                    downloader.update(threadId, downLength)//需要加入Update操作
                    downloader.appendSize(offset)//把新下载的数据长度加入到已经下载的数据总长度中
                    offset = inStream.read(buffer, 0, 1024)
                }
                threadFile.close()
                inStream.close()
                finish = true
            } catch (e: Exception) {
                downLength = -1
            }
        }
//        super.run()
    }

    fun isFinish(): Boolean {
        return finish
    }
}