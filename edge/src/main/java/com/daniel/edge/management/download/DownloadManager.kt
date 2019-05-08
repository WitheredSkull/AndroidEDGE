package com.daniel.edge.management.download

import android.os.Handler
import android.os.Message
import com.daniel.edge.management.download.constants.DownloadState
import com.daniel.edge.management.download.constants.OnDownloadListener
import com.daniel.edge.management.download.model.*
import com.daniel.edge.model.database.table.DownloadTable
import com.daniel.edge.model.database.table.DownloadThreadTable
import java.io.File
import java.lang.Exception

class DownloadManager {
    private var mDownload = arrayListOf<FileDownload>()
    var mMaxThreadNum = 3
    var mCallback: OnDownloadListener? = null
//    private var mHandler = object : Handler() {
//        override fun handleMessage(msg: Message) {
////            super.handleMessage(msg)
//            when (msg.what) {
//                DownloadState.Download.ordinal -> {
////                    mListener?.onProgress(msg.obj as String)
//                }
//                DownloadState.Success.ordinal -> {
//                    mListener?.onSuccess(msg.obj as String)
//                }
//                DownloadState.Stop.ordinal -> {
//                    mListener?.onStop(msg.obj as String)
//                }
//                DownloadState.Delete.ordinal -> {
//                    mListener?.onStop(msg.obj as String)
//                }
//            }
//        }
//    }

    fun setCallback(callback: OnDownloadListener):DownloadManager {
        this.mCallback = callback
        return this
    }

    fun getDownloadList(): ArrayList<DownloadModel> {
        val models = DownloadTable.getInstance().queryAll()
        models.forEach {
            it.threadModels = DownloadThreadTable.getInstance().query(it.id)
        }
        return models
    }

    fun stop(url: String) {
        val model = mDownload.find {
            it.getUrl().equals(url)
        }
        if (model != null) {
            model.stop()
            mDownload.remove(model)
            mCallback?.onStop(url, "手动暂停")
        }
    }

    fun stopAll() {
        mDownload.forEach {
            stop(it.getUrl())
        }
    }

    /**
     * 获取下载的状态
     */
    fun getState(url: String) {
        mDownload.find { it.getUrl().equals(url) }?.isExited
    }

    fun start(url: String, fileDir: File) {
        val model = DownloadTable.getInstance().queryFromURL(url)
        //如果已经下载过，更新一下下载的最新时间并且直接返回成功
        var saveFile: File? = null
        if (model != null && model.state != DownloadState.Delete.ordinal) {
            saveFile = File(model.filePath)
            if (saveFile.exists()) {
                model.currentTime = System.currentTimeMillis()
                if (model.state == DownloadState.Success.ordinal) {
                    DownloadTable.getInstance().update(model)
                    mCallback?.onSuccess(url)
                    return
                }
            } else {
                DownloadTable.getInstance().updateStateFromUrl(url,DownloadState.Delete)
            }
        }

        URLInfo(object : URLInfo.OnURLInfoCallback {
            override fun onSuccess(model: DownloadFileModel) {
                saveFile = File(fileDir, model.fileName)
                val download = FileDownload(model, saveFile!!, mCallback)
                mDownload.add(download)
                download.start()
                mCallback?.onStart(url)
            }

            override fun onFail() {
                mCallback?.onStop(url, "已经存在下载文件\n程序自动更新下载的最新时间")
            }
        }).execute(url)
    }

    fun startAll(waitDownloads: ArrayList<WaitDownloadInfo>) {
        waitDownloads.forEach {
            start(it.url, it.fileDir)
        }
    }

    companion object {
        fun getInstance() = Instance.INSTANCE
        var MaxDownloadNum = 3
    }

    object Instance {
        val INSTANCE = DownloadManager()
    }
}

data class WaitDownloadInfo(var url: String, var fileDir: File)