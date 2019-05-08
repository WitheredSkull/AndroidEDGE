package com.qiang.keyboard.viewModel

import android.app.Application
import com.lzy.okgo.model.Response
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.DeviceRequest
import com.qiang.keyboard.viewModel.base.BaseViewModel

class DeviceViewModel(application: Application):BaseViewModel(application) {
    init {
        getList()
    }

    fun getList(){
        DeviceRequest.setList(object :String64CallBack(){
            override fun success(code: Int, status: Int, des: String, body: Response<String>) {

            }
        })
    }
}