package com.daniel.edge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daniel.edge.R
import com.daniel.edge.Utils.Log.EdgeLog
import com.daniel.edge.constant.AppDatabase
import com.daniel.edge.model.modelDb.OpenDBEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        var b = "true"
        EdgeLog.show(javaClass,"值${b.toBoolean()}")
        Observable.create<String> {
            var appInfoList = AppDatabase.getInstance(applicationContext).getOpenDao().queryAll()
            if (appInfoList.any()) {
                AppDatabase.getInstance(applicationContext).getOpenDao().insert(OpenDBEntity("${System.currentTimeMillis()}",appInfoList[appInfoList.size-1].count + 1))
            }
            appInfoList.forEach {
                EdgeLog.show(javaClass,"第一次打印${it}")
            }
            AppDatabase.getInstance(applicationContext).getOpenDao().queryAll().forEach {
                EdgeLog.show(javaClass,"第二次打印${it}")
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .

    }

}
