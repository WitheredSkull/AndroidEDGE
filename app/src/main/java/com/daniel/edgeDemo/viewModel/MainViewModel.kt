package com.daniel.edgeDemo.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerHomeComponent
import com.daniel.edge.utils.log.EdgeLog
import javax.inject.Inject

class MainViewModel:ViewModel {

    var pwd = MutableLiveData<String>().apply {
        value = "123456"
    }
//    @Inject
//    lateinit var service: AccountService
    var text = MutableLiveData<String>().apply {
        value = "18888888888"
    }
    var userName = MutableLiveData<String>().apply {
        value = "18888888888"
    }

    fun onClick(view: View){
        when(view.id){
            R.id.button ->{
//                service.login(userName.value!!,pwd.value!!)
//                    .applyThread()
//                    .subscribeBy(onNext = {
//                        EdgeLog.show(javaClass,"登录结果",it)
//                    },onError = {
//
//                    })
            }
        }
    }

    @Inject
    constructor(){
        DaggerHomeComponent.builder().appComponent(App.appComponent).build().inject(this)
    }
}
