package com.shuanglu.edge.Management.Download.Model

import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import com.shuanglu.edge.Management.File.EdgeFileManagement
import java.io.File
import java.io.RandomAccessFile
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 11:27
 * @Description:
 *
 */
class EdgeDownAsyncTask : AsyncTask<String, Int, Boolean> {
    var model: EdgeDownloadModel
    var status:Boolean = true
    lateinit var file:File
    lateinit var fileFilter:File

    constructor(model: EdgeDownloadModel) : super() {
        this.model = model
    }

    fun start(){
        status = true
    }

    fun pause(){
        status = false
    }


    override fun doInBackground(vararg params: String?): Boolean {
        var huc = URL(params[0]).openConnection() as HttpURLConnection
        huc.requestMethod = "GET"
        huc.connectTimeout = 15000
        huc.readTimeout = 15000
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            model.totalSize = huc.contentLengthLong
        }else{
            model.totalSize = huc.contentLength.toLong()
        }
        val fileName = huc.getHeaderField("Content-Disposition")

        file = File(model.localPath)
        if (file.isDirectory){
            if (TextUtils.isEmpty(fileName)){
                model.localPath = model.localPath+System.currentTimeMillis()
            }else{
                model.localPath = model.localPath + fileName
            }
        }
        file = File(model.localPath)
        fileFilter = File(model.localPath + ".filter")
        if (!fileFilter.exists()){
            file = EdgeFileManagement.createFile(model.localPath!!)
        }
        model.name = file.name

        huc.setRequestProperty("Range","bytes=" + model.alreadyDownSize + "-" + model.totalSize)
        var randomFile = RandomAccessFile(file,"rwd")
        randomFile.seek(model.alreadyDownSize)
        if (huc.responseCode == HttpURLConnection.HTTP_PARTIAL){
            var buffer = ByteArray(512)
            var len = huc.inputStream.read(buffer)
            while (len != -1){
                if (status){
                    randomFile.write(buffer,0,len)
                    model.alreadyDownSize += len
                    publishProgress(BigDecimal(model.alreadyDownSize).divide(BigDecimal(model.totalSize)).toInt())
                }else{
                    //保存状态
                    return false
                }
            }
            randomFile.close()
            huc.inputStream.close()
            huc.disconnect()
        }
        return true
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
    }


    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result!!){
            EdgeFileManagement.deleteDirectoryAllData(fileFilter.absolutePath)
        }
    }
}