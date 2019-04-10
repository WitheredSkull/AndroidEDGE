package com.daniel.edge.view.webView.model

import android.webkit.JavascriptInterface
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils

class JavascriptResponse {
    @JavascriptInterface
    fun showToast(array: Array<String>) {
        kotlin.run {
            array.forEach {
                EdgeLog.show(javaClass, "资源嗅探", it)
            }
        }
    }
}