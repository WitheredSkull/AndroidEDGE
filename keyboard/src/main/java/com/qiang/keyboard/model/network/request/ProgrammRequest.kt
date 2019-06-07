package com.qiang.keyboard.model.network.request

import com.daniel.edge.management.application.EdgeApplicationManagement
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.Callback
import com.qiang.keyboard.model.network.api.AccountApi
import com.qiang.keyboard.model.network.api.ProgrammApi
import com.qiang.keyboard.utlis.MapConvert

object ProgrammRequest {

    fun <T>AppUpdate(callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("AppType","ANDROID")
        hashMap.put("Version", EdgeApplicationManagement.appVersionCode(EdgeApplicationManagement.appPackageName()).toString())
        return OkGo.post<T>(ProgrammApi.APP_Update.url).params("", MapConvert.jsonConvert(hashMap)).execute(callback)
    }
}