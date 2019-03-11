package com.daniel.edge.constant

import android.app.Application
import android.content.Context
import com.daniel.edge.EdgeManager
import com.daniel.edge.Management.Application.EdgeApplicationManagement

class App: Application() {
    companion object {
        lateinit var CONTEXT:Context
        const val DB_NAME = "app.info"
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        if (EdgeApplicationManagement.isMainProcess(this)){
            initEdge()
            initDataBase()
        }
    }

    fun initEdge(){
        EdgeManager.initEdge(this)
            .initActivityManagement()
            .initDemoLog()
            .initDemoToolBar()
            .initToast()
    }

    fun initDataBase(){
    }
}