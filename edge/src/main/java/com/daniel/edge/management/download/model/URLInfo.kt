package com.daniel.edge.management.download.model

import android.os.AsyncTask
import com.daniel.edge.config.Edge
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class URLInfo(var callback:OnURLInfoCallback) : AsyncTask<String, String, DownloadFileModel?>() {
    override fun doInBackground(vararg params: String?): DownloadFileModel? {
        try {
            val url = params[0]!!
            val httpURLConnection = RequestInfoUtils.request(url)
            var _fn = ""
            val _s = url.substring(url.lastIndexOf("/") + 1)
            if (!_s.isNullOrEmpty() || "".equals(_s.trim())) {
                httpURLConnection.headerFields.forEach {
                    if (!it.key.isNullOrEmpty() && "content-disposition".equals(it.key.toLowerCase())) {
                        //使用正则表达式查询文件名
                        val m = Pattern.compile(".*filename=(.*)").matcher(it.key.toLowerCase())
                        if (m.find()) {
                            _fn = m.group(1)//如果获取到正确的正则返回值则退出并返回
                            return@forEach
                        }
                    }
                }
            }
            if (_fn.isNullOrEmpty()) {
                _fn = "${System.currentTimeMillis()}"
            }
            val _l = httpURLConnection.contentLength
            return DownloadFileModel(url, _fn, _l)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(result: DownloadFileModel?) {
        super.onPostExecute(result)
        if (result!=null){
            callback.onSuccess(result)
        }else{
            callback.onFail()
        }
    }

    interface OnURLInfoCallback {
        fun onSuccess(model: DownloadFileModel)
        fun onFail()
    }
}