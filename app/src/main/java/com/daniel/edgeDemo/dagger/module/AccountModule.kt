package com.daniel.edgeDemo.dagger.module

import com.daniel.edgeDemo.model.service.AccountService
import com.daniel.edgeDemo.viewModel.AccountViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AccountModule {
    @Provides
    fun provideAccountService(retrofit: Retrofit):AccountService = retrofit.create(AccountService::class.java)

    @Singleton
    @Provides
    fun provideViewModel() = AccountViewModel()
}