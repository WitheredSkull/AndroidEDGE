package com.daniel.edgeDemo.dagger.component

import com.daniel.edgeDemo.dagger.module.AccountModule
import com.daniel.edgeDemo.view.account.AccountActivity
import com.daniel.edgeDemo.view.account.LoginFragment
import com.daniel.edgeDemo.view.account.RegisterFragment
import com.daniel.edgeDemo.viewModel.AccountViewModel
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