package com.daniel.edge.management.download.model

import android.os.Handler
import android.os.Message
import android.webkit.DownloadListener
import com.daniel.edge.management.download.DownloadManager
import com.daniel.edge.management.download.constants.DownloadState
import com.daniel.edge.management.download.DownloadThread
import com.daniel.edge.management.download.constants.OnDownloadListener
import com.daniel.edge.model.database.table.DownloadTable
import com.daniel.edge.model.database.table.DownloadThreadTable
import com.daniel.edge.utils.log.EdgeLog
import java.io.File
import java.math.BigDecimal

class FileDownload(
    downloadModel: DownloadFileModel,
    saveFile: File,
    callback: OnDownloadListener?
) {
    var mDownloadModel: DownloadFileModel = downloadModel
    var isExited = false
    var mState: DownloadState = DownloadState.Download
    var mLoadSize = 0L
    var mSaveFile: File = saveFile
    var threads: ArrayList<DownloadThread> = arrayListOf()
    var mModels = arrayListOf<DownloadThreadModel>()
    var mCallback = callback
    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DownloadState.Success.ordinal -> {
                    mCallback?.onSuccess(getUrl())
                }
                DownloadState.Stop.ordinal -> {
                    mCallback?.onStop(getUrl(), msg.obj as String)
                }
                DownloadState.Download.ordinal -> {
                    EdgeLog.show(javaClass,"进度","mProgress.toInt()")
                    mCallback?.onProgress(getUrl(), mProgress.toInt(), getLoadLength(), mDownloadModel.length.toLong())
                }
            }
        }
    }

    fun firstDownload() {
        //计算每一条线程需要下载的大小
        val _one_ds =
            BigDecimal(mDownloadModel.length).divide(
                BigDecimal(DownloadManager.MaxDownloadNum),
                0,
                BigDecimal.ROUND_HALF_DOWN
            ).toLong()
        val downloadTableId = DownloadTable.getInstance().insert(
            DownloadModel(
                0,
                mDownloadModel.fileName,
                mSaveFile.absolutePath,
                mDownloadModel.url,
                0,
                mDownloadModel.length.toLong(),
                DownloadState.Download.ordinal,
                System.currentTimeMillis()
            )
        )
        for (index in 1..DownloadManager.MaxDownloadNum) {
            val start = (index - 1) * _one_ds
            val end = if (DownloadManager.MaxDownloadNum == index) {
                mDownloadModel.length.toLong()
            } else {
                (index) * _one_ds
            }
            threads.add(
                DownloadThread(
                    this,
                    mDownloadModel.url,
                    mSaveFile,
                    start,
                    end
                )
            )
        }
        mModels.clear()
        threads.forEach {
            val threadModel = DownloadThreadModel(0, downloadTableId, it.startPosition, it.endPosition)
            val threadTableId = DownloadThreadTable.getInstance().insert(threadModel)
            it.downloadThreadId = threadTableId
            mModels.add(threadModel)
        }
    }

    fun againDownload(_m: DownloadModel) {
        mLoadSize = _m.loadSize
        val threadListModel = DownloadThreadTable.getInstance().query(_m.id)
        threadListModel.forEach {
            val downloadThread = DownloadThread(
                this,
                mDownloadModel.url,
                mSaveFile,
                it.startPosition,
                it.endPosition
            )
            downloadThread.downloadThreadId = it.id
            threads.add(downloadThread)
        }
        mModels.clear()
        mModels.addAll(threadListModel)
        mState = DownloadState.Download
        _m.state = mState.ordinal
        DownloadTable.getInstance().updateStateFromUrl(getUrl(), mState)
    }

    init {
        val _m = DownloadTable.getInstance().queryFromURL(mDownloadModel.url)
        //如果是已经开始下载的数据需要额外处理
        if (_m != null) {
            againDownload(_m)
        } else {
            firstDownload()
        }
    }

    fun getUrl(): String {
        return mDownloadModel.url
    }

    fun start() {
        mState = DownloadState.Download
        threads.forEach {
            it.start()
        }
    }

    fun stop() {
        mState = DownloadState.Stop
        isExited = true
    }


    fun onThreadExit(threadId: Long) {
        threads.find { it.downloadThreadId == threadId }?.let {
            threads.remove(it)
        }
        //如果线程全部关闭，无论是主动关闭还是被动关闭，当数量归零则开始回调
        if (threads.size == 0) {
            if (getLoadLength() == mDownloadModel.length.toLong()) {
                mState = DownloadState.Success
                mHandler.sendEmptyMessage(mState.ordinal)
            } else {
                mState = DownloadState.Stop
                val msg = Message()
                msg.what = mState.ordinal
                msg.obj = "下载出现异常"
                mHandler.sendMessage(msg)
            }
            //更新线程的状态
            DownloadTable.getInstance().updateStateFromUrl(mDownloadModel.url, mState)
        } else {
            mState = DownloadState.Download
        }
    }

//    fun callback1() {
//        when (mState) {
//            DownloadState.Success -> {
//                mListener?.onSuccess(getUrl())
//            }
//            DownloadState.Stop -> {
//                mListener?.onStop(getUrl())
//            }
//            DownloadState.Download -> {
//                if (mDownloadModel.length != 0) {
//                    val progress = BigDecimal(getLoadLength()).divide(
//                        BigDecimal(mDownloadModel.length),
//                        2,
//                        BigDecimal.ROUND_DOWN
//                    ).multiply(BigDecimal(100)).toInt()
//                    mListener?.onProgress(getUrl(), progress, getLoadLength(), mDownloadModel.length.toLong())
//                    EdgeLog.show(javaClass, "下载进度", "${progress} -- ${mLoadSize} -- ${mDownloadModel.length}")
//                }
//            }
//            else -> {
//
//            }
//        }
//    }

    fun getLoadLength(): Long {
        return mLoadSize
    }

    fun getThreadSize(): Int {
        return threads.size
    }


    var mProgress = 0f
    /**
     * 修改相对应的线程的下载大小
     */
    @Synchronized
    fun updateLoadSize(threadId: Long, startPosition: Long, loadSize: Long) {
        //修改下载线程的信息
        mModels.find {
            it.id == threadId
        }?.startPosition = startPosition
        //修改总下载的信息
        mState = DownloadState.Download
        mLoadSize += loadSize
        if (mDownloadModel.length != 0) {
            val progress = (getLoadLength().toFloat().div(mDownloadModel.length) * 100)
            if (mProgress - progress > 0.5) {
                mProgress = progress
                val msg = Message()
                msg.what = mState.ordinal
                mHandler.sendMessage(msg)
            }
        }

//        model?.let {
//            it.startPosition = pos
//            DownloadThreadTable.getInstance().update(it)
//            val time = System.currentTimeMillis()
//            if (time - upUpdateTime >= 400){
//                upUpdateTime = time
//            }
//        }
    }
}