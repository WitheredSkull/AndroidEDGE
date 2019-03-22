package com.daniel.edgeDemo.dagger.component

import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.module.HomeModule
import com.daniel.edgeDemo.view.MainActivity
import com.daniel.edgeDemo.view.MainFragment
import com.daniel.edgeDemo.viewModel.MainViewModel
import dagger.Component

@ActivityScoped
@Component(modules = arrayOf(HomeModule::class),dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(app:App)
    fun inject(activity:MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(viewModel: MainViewModel)
}