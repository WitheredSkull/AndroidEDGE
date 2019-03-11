package com.daniel.edge.constant

import android.app.Application
import android.content.Context
import com.daniel.edge.EdgeManager
import com.daniel.edge.dagger.component.AppComponent
import com.daniel.edge.dagger.component.DaggerAppComponent
import com.daniel.edge.dagger.component.DaggerHomeComponent
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.dagger.module.AppModule
import retrofit2.Retrofit
import javax.inject.Inject

class App: Application() {
    companion object {
        lateinit var CONTEXT:Context
        const val DB_NAME = "app.info"
    }

//    @Inject
//    lateinit var retrofit: Retrofit
    @Inject
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        if (EdgeApplicationManagement.isMainProcess(this)){
            DaggerHomeComponent.builder().appComponent(DaggerAppComponent.builder().appModule(AppModule(this)).build())
                .build().inject(this)

            initEdge()
            initDataBase()
            EdgeLog.show(javaClass,"检测单例","$context")
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