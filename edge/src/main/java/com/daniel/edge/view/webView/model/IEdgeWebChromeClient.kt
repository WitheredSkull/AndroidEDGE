package com.daniel.edge.view.webView.model

import android.content.Intent
import android.net.Uri

interface IEdgeWebChromeClient {
    fun onOpenFileResult(resultCode: Int,data:Intent?)
}