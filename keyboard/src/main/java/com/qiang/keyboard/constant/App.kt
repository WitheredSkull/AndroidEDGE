package com.qiang.keyboard.constant

import android.app.Application
import android.app.Service
import android.os.Vibrator
import com.daniel.edge.EdgeManager
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.qiang.keyboard.R
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (EdgeApplicationManagement.isMainProcess(this)) {
            EdgeManager.initEdge(this)
                .initActivityManagement()
                .initDemoLog()
                .initDemoToolBar()
                .initToast()
            initOkGo()
            initVibrator()
        }
    }

    private fun initVibrator() {
        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }

    fun initOkGo() {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
//log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
//log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.OFF)
        builder.addInterceptor(loggingInterceptor)
        //全局的读取超时时间
        builder.readTimeout(NetworkConfig.DEFAULT_TIME_OUT, TimeUnit.SECONDS);
//全局的写入超时时间
        builder.writeTimeout(NetworkConfig.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
//全局的连接超时时间
        builder.connectTimeout(NetworkConfig.DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS);
        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(CookieJarImpl(SPCookieStore(this)))
//使用数据库保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(CookieJarImpl(DBCookieStore(this)))
//使用内存保持cookie，app退出后，cookie消失
//        builder.cookieJar(CookieJarImpl(MemoryCookieStore()))
        //方法一：信任所有证书,不安全有风险
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
//        val headers = HttpHeaders()
//        headers.put("commonHeaderKey1", "commonHeaderValue1")    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2")
//        val params = HttpParams()
//        params.put("commonParamsKey1", "commonParamsValue1")     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数")
//-------------------------------------------------------------------------------------//
        okHttpClient = builder.build()
        OkGo.getInstance().init(this)                       //必须调用初始化
            .setOkHttpClient(okHttpClient)               //建议设置OkHttpClient，不设置将使用默认的
            .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
            .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//            .addCommonHeaders(headers)                      //全局公共头
//            .addCommonParams(params)                       //全局公共参数
    }

    companion object {
        val Base_Api = "https://api.qkey.link"

        //初始化OkHttp
        lateinit var okHttpClient: OkHttpClient
        //按键的文字大小，因为TextView文字会自动转换，所以不需要通过resources获取
        var textSize = 16f
        //振动服务
        lateinit var vibrator: Vibrator
    }
}