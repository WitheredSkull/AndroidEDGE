package com.daniel.edge.dagger.module

import com.daniel.edge.model.service.AccountService
import com.daniel.edge.viewModel.AccountViewModel
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