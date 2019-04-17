package com.daniel.edge.management.download.model

/**
 * @param state -1删除，0暂停，1开始
 */
data class DownloadModel(var id:Int,var name: String, var url: String,var state:Int = 0,var currentTime:Long) {
    var threadModels: ArrayList<DownloadThreadModel>? = null
}