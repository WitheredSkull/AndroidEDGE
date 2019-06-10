package com.qiang.keyboard.model.network.request

import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.Callback
import com.lzy.okgo.request.PostRequest
import com.qiang.keyboard.model.network.api.AccountApi
import com.qiang.keyboard.utlis.MapConvert

object AccountRequest {

    fun <T>getSMSVerify(mobile: String,callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("mobile", mobile)
        return OkGo.post<T>(AccountApi.SMS_VERIFY).params("", MapConvert.jsonConvert(hashMap)).execute(callback)
    }

    fun <T> register(mobile: String, pwd: String, code: String,callback: Callback<T>) {
        var hashMap = hashMapOf<String, String>()
        hashMap.put("mobile", mobile)
        hashMap.put("code", code)
        hashMap.put("password", pwd)
        return OkGo.post<T>(AccountApi.Register).params("", MapConvert.jsonConvert(hashMap)).execute(callback)
    }

    enum class DeviceType {
        //0为发送型设备，1为接收型设备
        Send,
        Receiver
    }

    fun <T>login(mobile: String, pwd: String, imei: String, type: DeviceType,callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("mobile", mobile)
        hashMap.put("password", pwd)
        hashMap.put("imei", imei)
        hashMap.put("type", type.ordinal.toString())
        return OkGo.post<T>(AccountApi.Login).params("", MapConvert.jsonConvert(hashMap)).execute(callback)
    }
}