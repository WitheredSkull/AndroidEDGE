package com.daniel.edge.model.database.table

data class DownloadModel(
    var id: Int,
    var name: String,
    var url: String,
    var thread_id: Int,
    var thread_download_size: Long,
    var thread_total_size: Long
)