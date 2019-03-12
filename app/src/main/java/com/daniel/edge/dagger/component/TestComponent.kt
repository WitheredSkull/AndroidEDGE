package com.daniel.edge.dagger.component

import com.daniel.edge.dagger.module.HomeModule
import com.daniel.edge.view.MainActivity
import dagger.Component

@Component(modules = arrayOf(HomeModule::class))
interface TestComponent {
//    fun inject(activity:MainActivity)
}