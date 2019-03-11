package com.daniel.edge.dagger.module

import android.content.Context
import android.util.Log
import com.daniel.edge.constant.AppDatabase
import com.daniel.edge.constant.NetworkConfig
import com.daniel.edge.dagger.component.ActivityScoped
import com.daniel.edge.model.network.apiPath.BaseApi
import com.daniel.edge.model.service.AccountService
import com.daniel.edge.retrofit.converterFactory.MultiFunctionConverterFactory
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
class AppModule(val applicationContext: Context) {
    @Provides
    fun provideContext():Context = applicationContext
}