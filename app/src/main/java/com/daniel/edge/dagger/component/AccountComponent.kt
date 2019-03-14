package com.daniel.edge.dagger.component

import com.daniel.edge.dagger.module.AccountModule
import com.daniel.edge.dagger.module.AppModule
import com.daniel.edge.view.account.AccountActivity
import com.daniel.edge.view.account.LoginFragment
import com.daniel.edge.view.account.RegisterFragment
import com.daniel.edge.viewModel.AccountViewModel
import dagger.Component
import javax.inject.Singleton

@ActivityScoped
@Singleton
@Component(modules = arrayOf(AccountModule::class),dependencies = arrayOf(AppComponent::class))
interface AccountComponent {
    fun getViewModel():AccountViewModel
    fun inject(activity:AccountActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(viewModel:AccountViewModel)
}