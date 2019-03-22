package com.daniel.edgeDemo.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.daniel.edgeDemo.R
import com.daniel.edgeDemo.constant.App
import com.daniel.edgeDemo.dagger.component.DaggerAccountComponent
import com.daniel.edgeDemo.model.enum.JumpFragmentEnum
import com.daniel.edgeDemo.model.rxJava.applyThread
import com.daniel.edgeDemo.model.service.AccountService
import com.daniel.edge.utils.toast.EdgeToastUtils
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class AccountViewModel : ViewModel {
    var userName = MutableLiveData<String>()
    var userPwd = MutableLiveData<String>()

    @Inject
    lateinit var accountService: AccountService

    @Inject
    constructor() {
        DaggerAccountComponent.builder().appComponent(App.appComponent).build().inject(this)
    }

    fun login(v: View) {
        if (userName.value != null && userPwd.value != null) {
            accountService.login(userName.value!!, userPwd.value!!)
                .applyThread()
                .subscribeBy(onError = {
                    it.printStackTrace()
                }, onNext = {
                    var controller = Navigation.findNavController(v)
                    controller.navigate(R.id.action_loginFragment_to_registerFragment)
                })
        }
    }

    fun register(v: View) {
        if (userName.value != null && userPwd.value != null) {
            accountService.register(userName.value!!, userPwd.value!!, userPwd.value!!)
                .applyThread()
                .subscribeBy(onError = {
                    it.printStackTrace()
                }, onNext = {
                    var controller = Navigation.findNavController(v)
                    controller.popBackStack()
                })
        }
    }

    fun jumpFragment(v:View,type:Int){

        EdgeToastUtils.getInstance().show("点击")
        when(type){
            JumpFragmentEnum.Login.ordinal ->
                Navigation.findNavController(v).popBackStack()
            JumpFragmentEnum.Register.ordinal ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
