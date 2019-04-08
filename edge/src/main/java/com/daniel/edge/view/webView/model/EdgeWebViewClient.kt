package com.daniel.edge.view.webView.model

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

// Create Time 2018/10/31
// Create Author Daniel 
class EdgeWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        var js = "Var length =0；\n" +
                "Var videoTags = document.getElementsByTagName（\"video\"）;\n" +
                "Var urlStrs = \"\"\n" +
                "For(var i=0;i<videoTags.length;i++){\n" +
                "       Try{\n" +
                "               If(videoTags[i].src.length !=0)\n" +
                "            {\n" +
                "                 urlStrs=urlStrs + videoTags[i].src + \",\";//获取所有video\n" +
                "                 length++;\n" +
                "            }catch(err){\n" +
                "}\n" +
                "}\n" +
                "alert(urlStrs)\n" +
                "}"
        view?.loadUrl("javascript:${js}")
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (view != null && request != null) {
                view.loadUrl(request.url.encodedPath)
            }
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return true
    }
}