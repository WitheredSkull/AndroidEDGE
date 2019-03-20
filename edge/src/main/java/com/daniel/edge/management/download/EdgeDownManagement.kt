package com.daniel.edge.management.download

import com.daniel.edge.management.download.model.EdgeDownAsyncTask
import com.daniel.edge.management.download.model.EdgeDownloadModel
import com.daniel.edge.model.database.table.DBTDownload

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 11:26
 * @Description:
 *
 */
class EdgeDownManagement {
    private lateinit var mDownTask:EdgeDownAsyncTask

    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    private object Instance{
        val INSTANCE = EdgeDownManagement()
    }
    fun down(localPath:String,tempPath:String):EdgeDownManagement{
        var model = EdgeDownloadModel(System.currentTimeMillis(),localPath,tempPath)
        var model2 = DBTDownload.getInstance().query(EdgeDownloadModel(0,localPath,tempPath))
        if (model2 != null) {
            model = model2
        }
        mDownTask = EdgeDownAsyncTask(model)
        mDownTask.execute(tempPath)
        return this
    }

    fun pause(){
        mDownTask.pause()
    }

    fun cancel(){
        mDownTask.cancel(true)
    }
}