package com.daniel.edge.management.download.model

data class DownloadThreadModel(
    var id: Long,
    var downloadId:Long,
    var startPosition:Long,
    var endPosition:Long
)