package com.daniel.edgeDemo.dagger.module

import com.daniel.edgeDemo.constant.AppDatabase
import com.daniel.edgeDemo.model.service.AccountService
import com.daniel.edgeDemo.viewModel.MainViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {
    @Provides
    fun provideMainViewModel() = MainViewModel()

    @Provides
    fun provideAccountService(client: Retrofit) = client.create(AccountService::class.java)

    /**
     * 获取打开次数的Dao
     */
    @Provides
    fun provideOpenDao(database: AppDatabase) = database.getOpenDao()

}