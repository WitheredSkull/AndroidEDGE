package com.daniel.edge.management.download.model

data class DownloadThreadModel(
    var id: Int,
    var downloadId:Int,
    var startPosition:Int,
    var endPosition:Int,
    var size: Int,
    var totalSize: Int
)