package com.daniel.edge.Management.Download

import com.daniel.edge.Management.Download.Model.EdgeDownAsyncTask
import com.daniel.edge.Management.Download.Model.EdgeDownloadModel
import com.daniel.edge.Model.Database.Table.DBTDownload

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