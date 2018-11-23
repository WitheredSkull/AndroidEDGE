package com.daniel.edge.View.WebView

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.IdRes
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import com.daniel.edge.View.WebView.Model.EdgeWebChromeClient
import com.daniel.edge.View.WebView.Model.EdgeWebViewClient
import java.io.File

// Create Time 2018/11/1
// Create Author Daniel 
class EdgeWebViewUtils(activity: Activity, @IdRes id: Int) {
    var activity: Activity
    var webView: WebView
    var edgeCachePath: String;
    lateinit var webSettings: WebSettings
    lateinit var webChromeClient: EdgeWebChromeClient
    lateinit var webViewClient: EdgeWebViewClient

    init {
        this.activity = activity
        webView = activity.findViewById(id)
        edgeCachePath = activity.externalCacheDir.absolutePath + "/EdgeWebCache";
    }

    companion object {
        fun build(activity: Activity, @IdRes id: Int): EdgeWebViewUtils {
            return EdgeWebViewUtils(activity, id)
        }
    }

    fun initDefaultSetting(): EdgeWebViewUtils {
        webSettings = webView.settings;
        //开启javascript
        webSettings.javaScriptEnabled = true
        //支持通过JS打开新窗口
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        /*LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
        LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式
        LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。*/
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        //设置缓存路径
        webSettings.setAppCachePath(edgeCachePath)
        /*我们能够有选择的缓冲web浏览器中所有的东西，从页面、图片到脚本、css等等。
        尤其在涉及到应用于网站的多个页面上的CSS和JavaScript文件的时候非常有用。其大小目前通常是5M。*/
        webSettings.setAppCacheEnabled(true)

        /*存储一些简单的用key/value对即可解决的数据，根据作用范围的不同，有Session
        Storage和Local Storage两种，分别用于会话级别的存储（页面关闭即消失）和本地化存储（除非主动*/
        webSettings.domStorageEnabled = true
        //开启database
        webSettings.databaseEnabled = true
        //设置数据库缓存地址
//        webSettings.databasePath =

        //设置默认编码
//        webSettings.defaultTextEncodingName = "utf-8"
        //WebView是否需要用户的手势进行媒体播放，默认值为true。
        webSettings.mediaPlaybackRequiresUserGesture = true
        //是否使用内置的缩放机制。内置的缩放机制包括屏幕上的缩放控件（浮于WebView内容之上）和缩放手势的运用。通过setDisplayZoomControls(boolean)可以控制是否显示这些控件，默认值为false。
        webSettings.builtInZoomControls = false
        //使用内置的缩放机制时是否展示缩放控件，默认值true。参见setBuiltInZoomControls(boolean).
        webSettings.displayZoomControls = false
//        (下面三项可能导致被攻击者攻击)
        // 设置是否允许 WebView 使用 File 协议
//        webSettings.allowFileAccess = true
        //设置是否允许通过 file url 加载的 Js代码读取其他的本地文件
//        webSettings.setAllowFileAccessFromFileURLs(true)
        //设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
//        webSettings.setAllowUniversalAccessFromFileURLs(true)
        //是否允许在WebView中访问内容URL（Content Url），默认允许。内容Url访问允许WebView从安装在系统中的内容提供者载入内容。
//        webSettings.allowContentAccess = true
        //是否允许WebView度超出以概览的方式载入页面，默认false。即缩小内容以适应屏幕宽度。该项设置在内容宽度超出WebView控件的宽度时生效，例如当getUseWideViewPort() 返回true时缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true

        //WebView是否保存表单数据，默认值true。
        webSettings.saveFormData = true
        //WebView是否保存密码
        webSettings.savePassword = true

        //设置页面上的文本缩放百分比，默认100。
//        webSettings.textZoom = 100
        //设置页面上的文字大小
//        webSettings.textSize

        //支持缩放
        webSettings.setSupportZoom(true)
        //默认缩放模式 是 ZoomDensity.MEDIUM
//        webSettings.defaultZoom = WebSettings.ZoomDensity.MEDIUM
        //WebView是否支持HTML的“viewport”标签或者使用wide viewport。设置值为true时，布局的宽度总是与WebView控件上的设备无关像素（device-dependent pixels）宽度一致。当值为true且页面包含viewport标记，将使用标签指定的宽度。如果页面不包含标签或者标签没有提供宽度，那就使用wide viewport。
        webSettings.useWideViewPort = true
        //设置WebView是否支持多窗口。如果设置为true，主程序要实现onCreateWindow(WebView, boolean, boolean, Message)，默认false。
        webSettings.setSupportMultipleWindows(true)
        //设置WebView是否支持多窗口。如果设置为true，主程序要实现onCreateWindow(WebView, boolean, boolean, Message)，默认false。
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS

        //WebView是否下载图片资源，默认为true。注意，该方法控制所有图片的下载，包括使用URI嵌入的图片（使用setBlockNetworkImage(boolean) 只控制使用网络URI的图片的下载）。如果该设置项的值由false变为true，WebView展示的内容所引用的所有的图片资源将自动下载。
        webSettings.loadsImagesAutomatically = true
        //是否禁止从网络（通过http和https URI schemes访问的资源）下载图片资源，默认值为false。注意，除非getLoadsImagesAutomatically()返回true,否则该方法无效。还请注意，即使此项设置为false，使用setBlockNetworkLoads(boolean)禁止所有网络加载也会阻止网络图片的加载。当此项设置的值从true变为false，WebView当前显示的内容所引用的网络图片资源会自动获取。
        webSettings.blockNetworkImage = false
        //是否禁止从网络下载数据，如果app有INTERNET权限，默认值为false，否则默认为true。使用setBlockNetworkImage(boolean) 只会禁止图片资源的加载。注意此值由true变为false，当前WebView展示的内容所引用的网络资源不会自动加载，直到调用了重载。如果APP没有INTERNET权限，设置此值为false会抛出SecurityException。
        webSettings.blockNetworkLoads = false

        //置WebView的用户代理字符串。如果字符串为null或者empty，将使用系统默认值。注意从KITKAT版本开始，加载网页时改变用户代理会让WebView再次初始化加载。
        webSettings.userAgentString = null
        //当一个安全的来源（origin）试图从一个不安全的来源加载资源时配置WebView的行为。默认情况下，KITKAT及更低版本默认值为MIXED_CONTENT_ALWAYS_ALLOW，LOLLIPOP版本默认值MIXED_CONTENT_NEVER_ALLOW，WebView首选的最安全的操作模式为MIXED_CONTENT_NEVER_ALLOW ，不鼓励使用MIXED_CONTENT_ALWAYS_ALLOW。
//        webSettings.mixedContentMode = MIXED_CONTENT_NEVER_ALLOW

        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(
                WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            );
        }
        return this
    }

    fun initDefaultClient(): EdgeWebViewUtils {
        webViewClient = EdgeWebViewClient();
        webView.webViewClient = webViewClient;
        return this
    }

    fun initDefaultChromeClient(): EdgeWebViewUtils {
        webChromeClient = EdgeWebChromeClient(activity as Context);
        webView.webChromeClient = webChromeClient
        return this
    }


    //设置浏览器UA
    fun setUserAgent(ua: String?): EdgeWebViewUtils {
        webSettings.userAgentString = ua
        return this
    }

    fun load(url: String): EdgeWebViewUtils {
        webView.loadUrl(url)
        return this
    }


    //清除浏览器缓存
    fun clearEdgeWebViewCache(): EdgeWebViewUtils {
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
        webView.clearMatches()
        webView.clearSslPreferences()
        var file = File(edgeCachePath)
        if (file.exists()) {
            file.deleteOnExit()
        }
        return this
    }


    fun closeWebView(): EdgeWebViewUtils {
        webView.destroy()
        activity.finish()
        return this
    }
}