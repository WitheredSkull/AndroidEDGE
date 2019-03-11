package com.daniel.edge.dagger.module

import android.content.Context
import android.util.Log
import com.daniel.edge.constant.AppDatabase
import com.daniel.edge.constant.NetworkConfig
import com.daniel.edge.dagger.component.ActivityScoped
import com.daniel.edge.model.network.apiPath.BaseApi
import com.daniel.edge.model.service.AccountService
import com.daniel.edge.retrofit.converterFactory.MultiFunctionConverterFactory
import com.daniel.edge.viewModel.MainViewModel
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class HomeModule {
    @Provides
    fun provideMainViewModel() = MainViewModel()


    @ActivityScoped
    @NotNull
    @Provides
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(NetworkConfig.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(NetworkConfig.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(NetworkConfig.DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(AppInterceptor())
        .build()

    @ActivityScoped
    @NotNull
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(MultiFunctionConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BaseApi.API)
        .build()

    @Provides
    fun provideAccountService(client: Retrofit) = client.create(AccountService::class.java)

    /**
     * 获取数据库管理
     */
    @Provides
    fun provideAppDatabase(applicationContext:Context) = AppDatabase.getInstance(applicationContext)

    /**
     * 获取打开次数的Dao
     */
    @Provides
    fun provideOpenDao(database: AppDatabase) = database.getOpenDao()

    /**
     * 网络日志打印类
     */
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