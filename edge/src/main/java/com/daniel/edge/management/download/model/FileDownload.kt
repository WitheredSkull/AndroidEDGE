package com.daniel.edge.management.download.model

import com.daniel.edge.management.download.DownloadThread
import com.daniel.edge.model.database.table.DownloadTable
import com.daniel.edge.model.database.table.DownloadThreadTable
import com.daniel.edge.utils.log.EdgeLog
import java.io.File
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.util.regex.Pattern

class FileDownload {
    var isExited = false
    var downloadSize = 0
    private var fileSize = 0
    var saveFile: File
    var downThreads: ArrayList<DownloadThread> = arrayListOf()
    var downloadFileModel: DownloadFileModel
    var downloadData = arrayListOf<DownloadThreadModel>()

    constructor(downloadFileModel: DownloadFileModel, saveDir: File, threadNum: Int = 1) {
        this.downloadFileModel = downloadFileModel
        var _one_ds =
            BigDecimal(downloadFileModel.length).divide(BigDecimal(threadNum), 0, BigDecimal.ROUND_HALF_DOWN).toLong()
        saveFile = File(saveDir, downloadFileModel.fileName)
        val _m = DownloadTable.getInstance().queryFromURL(downloadFileModel.url)
        if (_m != null) {
            val threadListModel = DownloadThreadTable.getInstance().query(_m.id)
            threadListModel.forEach {
                val downloadThread = DownloadThread(
                    this,
                    downloadFileModel.url,
                    saveFile,
                    it.startPosition,
                    it.endPosition
                )
                downloadThread.downloadThreadId = it.id
                downThreads.add(downloadThread)
            }
            _m.state = 1
            DownloadTable.getInstance().update(_m)
        } else {
            val downloadTableId = DownloadTable.getInstance().insert(
                DownloadModel(
                    0,
                    downloadFileModel.fileName,
                    downloadFileModel.url,
                    1,
                    System.currentTimeMillis()
                )
            )
            for (index in 1..threadNum) {
                val start = (index - 1) * _one_ds
                val end = if (threadNum == index) {
                    downloadFileModel.length.toLong()
                } else {
                    (index) * _one_ds
                }
                downThreads.add(
                    DownloadThread(
                        this,
                        downloadFileModel.url,
                        saveFile,
                        start,
                        end
                    )
                )
            }
            downThreads.forEach {
                val threadModel = DownloadThreadModel(0, downloadTableId, it.startPosition, it.endPosition)
                var threadTableId = DownloadThreadTable.getInstance().insert(threadModel)
                it.downloadThreadId = threadTableId
            }
        }
    }

    fun start() {
        downThreads.forEach {
            it.start()
        }
    }

    fun getDownloadLength(): Int {
        return downloadSize
    }

    fun getThreadSize(): Int {
        return downThreads.size
    }

    fun exit() {
        isExited = true
    }

    /**
     * 累计下载大小
     */
    @Synchronized
    fun appendSize(size: Int) {
        downloadSize += size
//        EdgeLog.show(javaClass,"${Thread.currentThread().name}下载进度","${downloadSize}")
    }

    @Synchronized
    fun update(threadId: Long, pos: Long) {
        val model = downloadData.find {
            it.id == threadId
        }
        model?.let {
            it.startPosition = pos
            DownloadThreadTable.getInstance().update(it)
        }
    }
}