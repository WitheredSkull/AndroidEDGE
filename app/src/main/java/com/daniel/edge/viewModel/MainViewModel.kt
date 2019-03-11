package com.daniel.edge.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daniel.edge.R
import com.daniel.edge.Utils.Log.EdgeLog
import com.daniel.edge.constant.applyThread
import com.daniel.edge.model.service.AccountService
import com.daniel.edge.retrofit.manager.RetrofitManager

class MainViewModel : ViewModel() {
    var text = MutableLiveData<String>().apply {
        value = "18888888888"
    }

    var userName = MutableLiveData<String>().apply {
        value = "18888888888"
    }
    var pwd = MutableLiveData<String>().apply {
        value = "123456"
    }

    fun onClick(view: View){
        when(view.id){
            R.id.button ->{
                RetrofitManager.getInstance().getService(AccountService::class.java)
                    .login(userName.value!!,pwd.value!!)
                    .applyThread()
                    .doOnError {
                        it.printStackTrace()
                    }
                    .flatMap {
                        EdgeLog.show(javaClass,"第一次请求 ${it}")
                        RetrofitManager.getInstance().getService(AccountService::class.java)
                            .login(userName.value!!,pwd.value!!)
                            .applyThread()
                    }
                    .subscribe {
                        EdgeLog.show(javaClass,"第二次请求 ${it}")
                    }
            }
        }
    }
}
