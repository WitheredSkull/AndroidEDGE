package com.shuanglu.edge.Management.Download.Model

import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import com.daniel.edge.Utils.Log.EdgeLog
import com.shuanglu.edge.Management.File.EdgeFileManagement
import com.shuanglu.edge.Model.Database.Table.DBTDownload
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.lang.Exception
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
    var DOWN_SUCCESS = 100
    var DOWN_PAUSE = -1
    var DOWN_FAILURE = -2
    var model: EdgeDownloadModel
    var status: Boolean = true
    lateinit var file: File
    lateinit var fileFilter: File

    constructor(model: EdgeDownloadModel) : super() {
        this.model = model
    }

    fun start() {
        status = true
    }

    fun pause() {
        status = false
    }


    override fun doInBackground(vararg params: String?): Boolean {
        try {
            var model2 = DBTDownload.getInstance().query(model)
            if (model2 != null) {
                model = model2
            }
            var huc = URL(params[0]).openConnection() as HttpURLConnection
            huc.requestMethod = "GET"
            huc.setRequestProperty("Range", "bytes=" + model.alreadyDownSize + "-")
            huc.connectTimeout = 15000
            huc.readTimeout = 15000
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                model.totalSize = huc.contentLengthLong
            } else {
                model.totalSize = huc.contentLength.toLong()
            }
            val disposition = huc.getHeaderField("Content-Disposition")
            var fileName = ""
            if (!TextUtils.isEmpty(disposition) && disposition.contains("filename")) {
                fileName = disposition.substring(disposition.indexOf("''") + 2, disposition.lastIndex)
            }

            EdgeLog.show(javaClass, fileName)
            file = File(model.localPath)
            if (file.isDirectory) {
                if (TextUtils.isEmpty(fileName)) {
                    model.localPath = model.localPath + System.currentTimeMillis()
                } else {
                    model.localPath = model.localPath + fileName
                }
            }
            file = File(model.localPath)
            fileFilter = File(model.localPath + ".filter")
            if (!fileFilter.exists()) {
                file = EdgeFileManagement.createFile(model.localPath!!)
                EdgeFileManagement.createFile(fileFilter.absolutePath)
            }
            model.name = file.name
            if (huc.responseCode == HttpURLConnection.HTTP_PARTIAL) {
                var randomFile = RandomAccessFile(file, "rwd")
                randomFile.seek(model.alreadyDownSize)
                var buffer = ByteArray(512)
                var len = huc.inputStream.read(buffer)
                DBTDownload.getInstance().insert(model)
                while (len != -1) {
                    if (status) {
                        randomFile.write(buffer, 0, len)
                        model.alreadyDownSize += len
                        publishProgress((BigDecimal(model.alreadyDownSize).toDouble() / BigDecimal(model.totalSize).toDouble() * 100).toInt())
                        DBTDownload.getInstance().update(model)
                        len = huc.inputStream.read(buffer)
                    } else {
                        publishProgress(DOWN_PAUSE)
                        //保存状态
                        return false
                    }
                }
                randomFile.close()
            } else if (huc.responseCode == HttpURLConnection.HTTP_OK) {
                var fileOutputStream = FileOutputStream(file.absolutePath)
                var buffer = ByteArray(512)
                var len = huc.inputStream.read(buffer)
                DBTDownload.getInstance().insert(model)
                while (len != -1) {
                    if (status) {
                        fileOutputStream.write(buffer, 0, len)
                        model.alreadyDownSize += len
                        publishProgress((BigDecimal(model.alreadyDownSize).toDouble() / BigDecimal(model.totalSize).toDouble() * 100).toInt())
                        DBTDownload.getInstance().update(model)
                        len = huc.inputStream.read(buffer)
                    } else {
                        publishProgress(DOWN_PAUSE)
                        //保存状态
                        return false
                    }
                }
                huc.disconnect()
            }
            fileFilter.delete()
            huc.inputStream.close()
            huc.disconnect()
            publishProgress(DOWN_SUCCESS)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            publishProgress(DOWN_FAILURE)
            return false
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        EdgeLog.show(javaClass, "进度" + values[0])
    }


    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result!!) {
            EdgeFileManagement.deleteDirectoryAllData(fileFilter.absolutePath)
        }
    }
}