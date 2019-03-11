package com.daniel.edge.retrofit.manager

import android.util.Log
import com.daniel.edge.retrofit.converterFactory.MultiFunctionConverterFactory
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class RetrofitManager {


    //默认读取超时时长
    private val DEFAULT_READ_TIME_OUT = 10L
    //默认超时时长
    private val DEFAULT_TIME_OUT = 5L
    //默认写入超时时长
    private val DEFAULT_WRITE_TIME_OUT = 10L
    private var httpBuild: OkHttpClient
    private var retrofitBuild: Retrofit

    //因为Retrofit内部实现过了class的缓存，所以这里不做任何操作
    fun <T> getService(clazz: Class<T>): T {
        return retrofitBuild.create(clazz)
    }

    init {
        //创建OkHttpClient
        httpBuild = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(AppInterceptor())
            .build()
        retrofitBuild = Retrofit.Builder()
            .client(httpBuild)
            .addConverterFactory(MultiFunctionConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://wanandroid.com/")
            .build()
    }

    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    object Instance {
        val INSTANCE = RetrofitManager()
    }

    inner class AppInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.i("EDGE请求地址", "${chain.request().url()}")
            Log.i("EDGE请求方式", "${chain.request().method()}")
            var buffer = okio.Buffer()
            chain.request().body()?.writeTo(buffer)
            Log.i("EDGE请求参数", "${buffer.readString(Charset.defaultCharset())}")
            return chain.proceed(chain.request())
        }

    }
}