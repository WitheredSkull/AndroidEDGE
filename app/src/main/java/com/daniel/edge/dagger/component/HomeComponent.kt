package com.daniel.edge.dagger.component

import com.daniel.edge.constant.App
import com.daniel.edge.dagger.module.HomeModule
import com.daniel.edge.view.MainActivity
import com.daniel.edge.view.MainFragment
import com.daniel.edge.viewModel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@ActivityScoped
@Component(modules = arrayOf(HomeModule::class),dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(app:App)
    fun inject(activity:MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(viewModel: MainViewModel)
}