package com.daniel.edge.management.download.model

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 11:19
 * @Description:
 *
 */
class EdgeDownloadModel {
    var id:Long? = null
    var startTime: Long? = null
    var name: String? = null
    var localPath: String? = null
    var tempPath: String? = null
    var totalSize: Long = 0
    var alreadyDownSize: Long = 0

    constructor(startTime: Long?, localPath: String?, tempPath: String?) {
        this.startTime = startTime
        this.localPath = localPath
        this.tempPath = tempPath
    }
}