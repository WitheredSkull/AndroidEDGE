package com.daniel.edgeDemo.constant

import android.app.Application
import android.content.Context
import com.daniel.edge.EdgeManager
import com.daniel.edgeDemo.dagger.component.AppComponent
import com.daniel.edgeDemo.dagger.component.DaggerAppComponent
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edgeDemo.dagger.module.AppModule

class App : Application() {
    companion object {
        const val DB_NAME = "app.info"
        lateinit var CONTEXT: Context
        var appComponent: AppComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        if (EdgeApplicationManagement.isMainProcess(this)) {
            if (appComponent == null) {
                appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
            }
            initEdge()
            initDataBase()
        }
    }

    fun initEdge() {
        EdgeManager.initEdge(this)
            .initActivityManagement()
            .initDemoLog()
            .initDemoToolBar()
            .initToast()
    }

    fun initDataBase() {
    }
}