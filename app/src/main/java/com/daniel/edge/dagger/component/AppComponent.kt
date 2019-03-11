package com.daniel.edge.dagger.component

import android.app.Application
import android.content.Context
import androidx.databinding.DataBindingComponent
import com.daniel.edge.constant.App
import com.daniel.edge.dagger.module.AppModule
import com.daniel.edge.dagger.module.HomeModule
import com.daniel.edge.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent{
    fun getContext():Context
}