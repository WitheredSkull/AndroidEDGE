package com.daniel.edge.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daniel.edge.R
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.constant.AppDatabase
import com.daniel.edge.constant.applyThread
import com.daniel.edge.dagger.component.DaggerAppComponent
import com.daniel.edge.dagger.component.DaggerHomeComponent
import com.daniel.edge.dagger.module.AppModule
import com.daniel.edge.dagger.module.HomeModule
import com.daniel.edge.model.modelDb.OpenDBEntity
import com.daniel.edge.viewModel.MainViewModel
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var mViewModel: MainViewModel
//    @Inject
//    lateinit var retrofit: Retrofit
    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        DaggerHomeComponent.builder()
            .appComponent(DaggerAppComponent.builder().appModule(AppModule(this)).build())
            .build().inject(this)

//        DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)
//        DaggerHomeComponent.builder().homeModule(HomeModule()).build().inject(this)

        EdgeLog.show(javaClass, "检测单例2", "${context}")
//
//        mViewModel.userName.observeForever {
//            EdgeLog.show(javaClass, "测试新的值", "${it}")
//        }
//
//        mViewModel.userName.value = "1"
//        mViewModel.userName.value = "3"
//        mViewModel.userName.value = "2"
//        mViewModel.userName.value = "4"

        var b = "true"
        EdgeLog.show(javaClass, "值","${b.toBoolean()}")
        Observable.create<String> {
            var appInfoList = AppDatabase.getInstance(applicationContext).getOpenDao().queryAll()
            if (appInfoList.any()) {
                AppDatabase.getInstance(applicationContext).getOpenDao()
                    .insert(OpenDBEntity("${System.currentTimeMillis()}", appInfoList[appInfoList.size - 1].count + 1))
            } else {
                AppDatabase.getInstance(applicationContext).getOpenDao()
                    .insert(OpenDBEntity("${System.currentTimeMillis()}", 0))
            }
            appInfoList.forEach {
                EdgeLog.show(javaClass, "第一次打印","${it}")
            }
            AppDatabase.getInstance(applicationContext).getOpenDao().queryAll().forEach {
                EdgeLog.show(javaClass, "第二次打印","${it}")
            }
        }.applyThread()
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//            .subscribe {
//            }

    }

}
