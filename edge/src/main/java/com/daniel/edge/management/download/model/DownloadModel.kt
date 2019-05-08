package com.daniel.edge.management.download.model

import com.daniel.edge.management.download.constants.DownloadState

/**
 * @param state //0下载,1停止,2完成,3删除
 */
data class DownloadModel(var id:Long, var name: String,var filePath:String, var url: String, var loadSize:Long, var length:Long, var state:Int = DownloadState.Download.ordinal, var currentTime:Long) {
    var threadModels: ArrayList<DownloadThreadModel>? = null
}