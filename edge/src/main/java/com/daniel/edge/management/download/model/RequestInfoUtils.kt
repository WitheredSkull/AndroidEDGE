package com.daniel.edge.management.download.model

import com.daniel.edge.config.Edge
import com.daniel.edge.utils.log.EdgeLog
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

object RequestInfoUtils {

    /**
     * 获取网络文件信息
     * @return 返回信息
     */
    fun getURLFileInfo(url: String): DownloadFileModel {
        val httpURLConnection = request(url)
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
        if (_fn.isNullOrEmpty()){
            _fn = "${System.currentTimeMillis()}"
        }
        val _l = httpURLConnection.contentLength
        return DownloadFileModel(url,_fn,_l)
    }

    /**
     * 获取请求
     */
    fun request(url: String): HttpURLConnection {
        val _u = URL(url)
        val httpURLConnection = _u.openConnection() as HttpURLConnection
        httpURLConnection.connectTimeout = Edge.CONNECT_TIMEOUT
        httpURLConnection.readTimeout = Edge.READ_TIMEOUT
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty(
            "Accept",
            "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"
        )
        httpURLConnection.setRequestProperty("Accept-Language", "zh-CN")
        httpURLConnection.setRequestProperty("Referer", url)
        httpURLConnection.setRequestProperty("Charset", "UTF-8")
        httpURLConnection.setRequestProperty(
            "User-Agent",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"
        )
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive")
        httpURLConnection.connect()
        if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
        } else {
            throw Exception("返回码非HTTP_OK${httpURLConnection.responseCode}")
        }
        return httpURLConnection
    }
}