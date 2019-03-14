package com.daniel.edge.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daniel.edge.R
import com.daniel.edge.constant.App
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.constant.AppDatabase
import com.daniel.edge.dagger.component.DaggerHomeComponent
import com.daniel.edge.model.rxJava.applyThread
import com.daniel.edge.model.modelDb.OpenDBEntity
import com.daniel.edge.model.rxJava.AppObserver
import com.daniel.edge.model.service.AccountService
import com.daniel.edge.retrofit.manager.RetrofitManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var service: AccountService

    @Inject
    lateinit var okHttpClient: OkHttpClient
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        DaggerHomeComponent.builder()
            .appComponent(App.appComponent)
            .build().inject(this)

        EdgeLog.show(javaClass, "检测单例1", "${okHttpClient} == ${retrofit}")

//        service.register("123456", "123456","123456")
//            .applyThread()
//            .subscribeBy(onError = {
//                it.printStackTrace()
//            },onNext = {
//                EdgeLog.show(javaClass,"登录",it)
//            })

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
        EdgeLog.show(javaClass, "值", "${b.toBoolean()}")
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
                EdgeLog.show(javaClass, "第一次打印", "${it}")
            }
            AppDatabase.getInstance(applicationContext).getOpenDao().queryAll().forEach {
                EdgeLog.show(javaClass, "第二次打印", "${it}")
            }
        }.applyThread()
//            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//            .subscribe {
//            }

    }

}
