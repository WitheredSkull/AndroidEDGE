package com.daniel.edge.management.download.constants

interface OnDownloadListener {
    //开始
    fun onStart(url: String)

    //进度
    fun onProgress(url: String, progress: Int, loadSize: Long, length: Long)

    //结束
    fun onStop(url: String,error:String)

    //下载完成
    fun onSuccess(url: String)
}