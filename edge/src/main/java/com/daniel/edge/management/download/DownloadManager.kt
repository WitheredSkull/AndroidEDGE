package com.daniel.edge.management.download

import com.daniel.edge.management.download.model.DownloadModel
import com.daniel.edge.management.download.model.FileDownload
import com.daniel.edge.management.download.model.RequestInfoUtils
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.model.database.table.DownloadTable
import com.daniel.edge.model.database.table.DownloadThreadTable
import java.io.File
import java.math.BigDecimal

class DownloadManager {
    private var mDownload = arrayListOf<FileDownload>()

    fun getDownloadList(): ArrayList<DownloadModel> {
        val models = DownloadTable.getInstance().queryAll()
        models.forEach {
            it.threadModels = DownloadThreadTable.getInstance().query(it.id)
        }
        return models
    }

    fun down(url: String, saveDir: File, threadNum: Int = 1):DownloadManager {
        var info = RequestInfoUtils.getURLFileInfo(url)
        mDownload.add(FileDownload(info,saveDir,threadNum))
        return this
    }

    fun allStart(){
        mDownload.forEach {
            it.start()
        }
    }

    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    object Instance {
        val INSTANCE = DownloadManager()
    }
}