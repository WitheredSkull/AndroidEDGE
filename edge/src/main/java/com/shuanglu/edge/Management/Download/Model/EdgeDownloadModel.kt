package com.shuanglu.edge.Management.Download.Model

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 11:19
 * @Description:
 *
 */
class EdgeDownloadModel {
    var id:Int = 0
    var name: String? = null
    var localPath: String? = null
    var tempPath: String? = null
    var totalSize: Long = 0
    var alreadyDownSize: Long = 0

    constructor(name: String?, localPath: String?, tempPath: String?) {
        this.name = name
        this.localPath = localPath
        this.tempPath = tempPath
    }
}