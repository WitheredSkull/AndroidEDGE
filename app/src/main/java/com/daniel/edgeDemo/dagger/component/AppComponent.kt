package com.daniel.edgeDemo.dagger.component

import android.content.Context
import com.daniel.edgeDemo.constant.AppDatabase
import com.daniel.edgeDemo.dagger.module.AppModule
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Component(modules = arrayOf(AppModule::class))
interface AppComponent{
    fun getContext():Context
    fun provideAppDatabase():AppDatabase
    fun provideOkHttp():OkHttpClient
    fun provideRetrofit():Retrofit
}