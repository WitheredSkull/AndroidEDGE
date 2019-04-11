package com.daniel.edge.view.webView.model

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.daniel.edge.R
import com.daniel.edge.config.Edge
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.window.dialog.IEdgeDialogCallback
import com.daniel.edge.window.dialog.bottomSheetDialog.EdgeBottomSheetDialogFragment
import com.daniel.edge.window.dialog.bottomSheetDialog.model.OnEdgeDialogClickListener
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URLDecoder

// Create Time 2018/10/31
// Create Author Daniel 
class EdgeWebViewClient : WebViewClient, OnEdgeDialogClickListener {

    var activity: WeakReference<FragmentActivity>
    var mCacheOpenAppName: String? = null
    var mCacheOpenIntent: Intent? = null
    override fun onClick(parent: View, view: View, dialog: Dialog) {
        when (view.id) {
            R.id.tv_allow -> {
                onOpenAppAllow()
            }
            R.id.tv_reject -> {
            }
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        //做完操作后需要删除记录保证程序的稳定运行
        mCacheOpenAppName = null
        mCacheOpenIntent = null
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val js =
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
            startActivity(url)
            return true
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

    open fun onOpenAppAllow() {
        try {
            Edge.CONTEXT.startActivity(mCacheOpenIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
        }
    }

    fun startActivity(url: String) {
        mCacheOpenIntent = Intent()
        mCacheOpenIntent?.action = Intent.ACTION_VIEW
        val uri = Uri.parse(url)
        mCacheOpenIntent?.data = uri
        var deCodeUrl = URLDecoder.decode(url)
        if (deCodeUrl.contains(WEBCHAT_PAY_SCHEME)) {
            mCacheOpenAppName = "微信"
        } else if (deCodeUrl.contains(ALIPAYS_SCHEME)) {
            mCacheOpenAppName = "支付宝"
        } else if (deCodeUrl.contains(Baidu_SCHEME)) {
            mCacheOpenAppName = "百度"
        } else if (deCodeUrl.contains("package")) {
            deCodeUrl = deCodeUrl.substring(deCodeUrl.indexOf("package=") + 8, deCodeUrl.length)
            deCodeUrl = deCodeUrl.substring(0, deCodeUrl.indexOf(";"))
            mCacheOpenAppName = EdgeApplicationManagement.appName(deCodeUrl)
            if (mCacheOpenAppName.isNullOrEmpty() && !deCodeUrl.isNullOrEmpty()) {
                mCacheOpenAppName = deCodeUrl
            }
        }

        activity.get()?.let {
            EdgeBottomSheetDialogFragment.build(it.supportFragmentManager, R.layout.dialog_baise)
                .setTransparencyBottomSheetDialog()
                .addOnClick(this, R.id.tv_allow, R.id.tv_reject)
                .setDialogCallback(object : IEdgeDialogCallback {
                    override fun onDialogDismiss() {

                    }

                    override fun onDialogDisplay(v: View?, dialog: Dialog) {
                        v?.findViewById<TextView>(R.id.tv_content)?.text =
                            if (EdgeTextUtils.isEmpty(mCacheOpenAppName)) {
                                Edge.CONTEXT.getString(R.string.open_external_app_format)
                            } else {
                                String.format(Edge.CONTEXT.getString(R.string.open_app_format), mCacheOpenAppName)
                            }
                    }
                })
                .show()
        }
    }

    constructor(activity: WeakReference<FragmentActivity>) : super() {
        this.activity = activity
    }

    companion object {
        /**
         * 支付宝
         */
        val ALIPAYS_SCHEME = "alipays://"
        /**
         * 百度
         */
        val Baidu_SCHEME = "baiduboxapp://"
        /**
         * https scheme
         */
        val HTTPS_SCHEME = "https://"
        /**
         * http scheme
         */
        val HTTP_SCHEME = "http://"
        /**
         * intent ' s scheme
         */
        val INTENT_SCHEME = "intent://"
        /**
         * Wechat pay scheme ，用于唤醒微信支付
         */
        val WEBCHAT_PAY_SCHEME = "weixin://wap/pay?"
    }
}