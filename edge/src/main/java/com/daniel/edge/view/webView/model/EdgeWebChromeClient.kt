package com.daniel.edge.view.webView.model

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.*
import androidx.fragment.app.FragmentActivity
import com.daniel.edge.config.Edge
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.OnEdgePermissionCallBack
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.viewUtils.EdgeViewHelperUtils
import java.lang.ref.WeakReference
import com.daniel.edge.R
import com.daniel.edge.management.file.EdgeFileManagement
import com.daniel.edge.utils.photo.EdgePhotoUtils
import com.daniel.edge.utils.photo.OnPhotoListener
import com.daniel.edge.utils.photo.PhotoMethod
import java.lang.Exception


// Create Time 2018/10/30
// Create Author Daniel 
class EdgeWebChromeClient : WebChromeClient, OnPhotoListener {
    override fun onChooseUri(uris: Array<Uri>?) {
        mUploadMessageAboveL?.onReceiveValue(uris)
        mUploadMessageAboveL = null
    }

    var activity: WeakReference<FragmentActivity>
    var mUploadMessageAboveL: ValueCallback<Array<Uri>>? = null
    var mWebTitle: String? = null
    var mWebView: WeakReference<WebView>
    override fun onShowCustomView(view: View?, requestedOrientation: Int, callback: CustomViewCallback?) {
        super.onShowCustomView(view, requestedOrientation, callback)
    }

    //视频（video）控件在没有播放的时候将给用户展示一张“海报”图片（预览图）。其预览图是由Html中video标签的poster属性来指定的。如果开发者没有设置poster属性, 则可以通过这个方法来设置默认的预览图。
    override fun getDefaultVideoPoster(): Bitmap? {
        return super.getDefaultVideoPoster()
    }

    //播放视频时，在第一帧呈现之前，需要花一定的时间来进行数据缓冲。ChromeClient可以使用这个函数来提供一个在数据缓冲时显示的视图。 例如,ChromeClient可以在缓冲时显示一个转轮动画。
    override fun getVideoLoadingProgressView(): View? {
        return super.getVideoLoadingProgressView()
    }

    //获得所有访问历史项目的列表，用于链接着色。
    override fun getVisitedHistory(p0: ValueCallback<Array<String>>?) {
        super.getVisitedHistory(p0)
    }

    //通知应用程序从关闭传递过来的webview并从view tree中remove。
    override fun onCloseWindow(p0: WebView?) {
        super.onCloseWindow(p0)
    }

    //控制台輸出
    override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
        EdgeLog.show(javaClass, "打印日志", p0?.message())
        return true
    }

    //请求创建一个新的窗口，如果我们应用程序接管这个请求，必须返回true，并且创建一个新的webview来承载主窗口。
    //如果应用程序不处理，则需要返回false，默认行为和返回false表现一样。
    override fun onCreateWindow(p0: WebView?, p1: Boolean, p2: Boolean, p3: Message?): Boolean {
        return super.onCreateWindow(p0, p1, p2, p3)
    }

    //当前页面请求是否允许进行定位。
    override fun onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt()
    }

    //当前页面请求是否允许进行定位。
    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        EdgeLog.show(javaClass, "获取权限", "需要")
        activity.get()?.let {
            EdgePermissionManagement.Build().setOnCallBack(object : OnEdgePermissionCallBack {
                override fun onRequestPermissionSuccess() {
                    EdgeViewHelperUtils.showBasicsDialog(
                        it.supportFragmentManager, R.layout.dialog_baise, Edge.CONTEXT.getString(R.string.edge_tip),
                        String.format(
                            Edge.CONTEXT.getString(R.string.need_location_format),
                            if (EdgeTextUtils.isEmpty(mWebTitle)) {
                                ""
                            } else {
                                mWebTitle
                            }
                        ),
                        Edge.CONTEXT.getString(R.string.allow),
                        Edge.CONTEXT.getString(R.string.reject),
                        object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                callback?.invoke(origin, true, true)
                                mWebView.get()?.reload()
                            }
                        },
                        null
                    )
                }

                override fun onRequestPermissionFailure(permissions: ArrayList<String>) {
                    var haveLocation = true
                    permissions.forEach {
                        if (it.contains("LOCATION")) {
                            haveLocation = false
                        }
                    }
                    if (haveLocation) {
                        onRequestPermissionSuccess()
                    }
                }

            }).requestPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                .build()
        }
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    //退出视频通知
    override fun onHideCustomView() {
        super.onHideCustomView()
    }

    //通知应用程序显示javascript alert对话框，如果应用程序返回true内核认为应用程序处理这个消息，返回false，内核自己处理。
    // Tips: 如果我们应用接管处理， 则必须给出result的结果，result.cancel,result.comfirm必须调用其中之后，否则内核会hang住。
    override fun onJsAlert(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
        return super.onJsAlert(p0, p1, p2, p3)
    }

    //通知应用程序显示一个对话框，让用户选择是否离开当前页面，这个回调是javascript中的onbeforeunload事件，如果客户端返回true，内核会认为客户端提供对话框。默认行为是return false。
    override fun onJsBeforeUnload(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
        return super.onJsBeforeUnload(p0, p1, p2, p3)
    }

    //通知应用程序提供confirm 对话框。
    override fun onJsConfirm(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
        return super.onJsConfirm(p0, p1, p2, p3)
    }

    //通知应用程序提供confirm 对话框。
    override fun onJsPrompt(p0: WebView?, p1: String?, p2: String?, p3: String?, p4: JsPromptResult?): Boolean {
        return super.onJsPrompt(p0, p1, p2, p3, p4)
    }

    //JS响应超時
    override fun onJsTimeout(): Boolean {
        return super.onJsTimeout()
    }


    //通知应用程序当前网页加载的进度
    override fun onProgressChanged(p0: WebView?, p1: Int) {
        super.onProgressChanged(p0, p1)
//        EdgeLog.show(javaClass, "进度", "${p1}")
    }

    //获取网页Icon
    override fun onReceivedIcon(p0: WebView?, p1: Bitmap?) {
        super.onReceivedIcon(p0, p1)
    }

    //获取网页标题
    override fun onReceivedTitle(p0: WebView?, p1: String?) {
        mWebTitle = p1
        super.onReceivedTitle(p0, p1)
    }

    //如果应用程序需要这个icon的话， 可以通过这个url获取得到 icon。
    override fun onReceivedTouchIconUrl(p0: WebView?, p1: String?, p2: Boolean) {
        super.onReceivedTouchIconUrl(p0, p1, p2)
    }

    //webview请求得到focus，发生这个主要是当前webview不是前台状态，是后台webview
    override fun onRequestFocus(p0: WebView?) {
        super.onRequestFocus(p0)
    }

    //通知应用程序webview需要显示一个custom view，主要是用在视频全屏HTML5Video support。
    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
    }

    //请求打开上传文件
    override fun onShowFileChooser(p0: WebView?, p1: ValueCallback<Array<Uri>>?, p2: FileChooserParams?): Boolean {
        mUploadMessageAboveL = p1
        try {
            openImageChooserActivity()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return super.onShowFileChooser(p0, p1, p2)
        }
    }

    private fun openImageChooserActivity() {
        EdgePhotoUtils()
            .setOnPhotoListener(this)
            .setPhotoPath("${EdgeFileManagement.getEdgeExternalDCIMPath()}/${System.currentTimeMillis()}.jpg")
            .setChooseMethod(PhotoMethod.ALL)
            .build()
    }

    constructor(activity: WeakReference<FragmentActivity>, webView: WebView) : super() {
        this.mWebView = WeakReference(webView)
        this.activity = activity
    }

    companion object {
    }
}

