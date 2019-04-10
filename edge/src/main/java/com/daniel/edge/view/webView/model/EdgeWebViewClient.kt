package com.daniel.edge.view.webView.model

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.daniel.edge.config.EdgeConfig
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils
import java.lang.Exception
import java.net.URLDecoder

// Create Time 2018/10/31
// Create Author Daniel 
class EdgeWebViewClient : WebViewClient() {
    companion object {
        /**
         * intent ' s scheme
         */
        val INTENT_SCHEME = "intent://"
        /**
         * Wechat pay scheme ，用于唤醒微信支付
         */
        val WEBCHAT_PAY_SCHEME = "weixin://wap/pay?"
        /**
         * 支付宝
         */
        val ALIPAYS_SCHEME = "alipays://"
        /**
         * http scheme
         */
        val HTTP_SCHEME = "http://"
        /**
         * https scheme
         */
        val HTTPS_SCHEME = "https://"
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        var js =
            "var child = document.children;" +
                    "var arr = [];" +
                    "function findSrc(obj) {" +
                    "for (var i = 0; i < obj.length; i++) {" +
                    "if (obj[i].children) {" +
                    "findSrc(obj[i].children);" +
                    "}" +
                    "var srcNotNull = obj[i].getAttribute(\"src\");" +
                    "if(srcNotNull != null&&srcNotNull.length>0){" +
                    "arr.push(srcNotNull);" +
                    "}" +
                    "}" +
                    "}" +
                    "findSrc(child);" +
                    "console.log(arr);" +
                    "window.android.showToast(arr);"
        view?.loadUrl("javascript:${js}")
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        if (url.isNullOrEmpty()) {
            return false
        } else if (url.startsWith(HTTP_SCHEME) || url.startsWith(HTTPS_SCHEME)) {
            return false
        } else {
            try {
                startActivity(url)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                return true
            }
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (view != null && request != null) {
//                view.loadUrl(request.url.encodedPath)
//            }
//        }
//        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        return super.shouldInterceptRequest(view, url)
    }

    fun startActivity(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        var uri = Uri.parse(url)
        var deCodeUrl = URLDecoder.decode(url)
        var appName: String? = null
        if (deCodeUrl.contains("package")) {
            deCodeUrl = deCodeUrl.substring(deCodeUrl.indexOf("package=") + 8, deCodeUrl.length)
            deCodeUrl = deCodeUrl.substring(0, deCodeUrl.indexOf(";"))
            appName = EdgeApplicationManagement.appName(deCodeUrl)
        } else if (deCodeUrl.contains(WEBCHAT_PAY_SCHEME)) {
            appName = "微信"
        } else if (deCodeUrl.contains(ALIPAYS_SCHEME)) {
            appName = "支付宝"
        }
        if (appName.isNullOrEmpty()){
            EdgeToastUtils.getInstance().show(appName!!)
        }
        intent.data = uri
        EdgeConfig.CONTEXT.startActivity(intent)
    }
}