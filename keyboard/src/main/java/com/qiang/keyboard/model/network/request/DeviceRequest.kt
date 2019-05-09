package com.qiang.keyboard.model.network.request

import com.daniel.edge.utils.system.EdgeSystemUtils
import com.lzy.okgo.OkGo
import com.qiang.keyboard.model.network.api.DeviceApi
import com.qiang.keyboard.utlis.MapConvert
import com.lzy.okgo.callback.Callback
import com.qiang.keyboard.model.data.AccountData

object DeviceRequest {
    /**
     * 设置设备昵称
     */
    fun <T> setNickName(nickName: String,imei:String, callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("sessionid", AccountData.getSession())
        hashMap.put("imei", imei)
        hashMap.put("nickname", nickName)
        return OkGo.post<T>(DeviceApi.SET_NIKE_NAME.url).params("", MapConvert.jsonConvert(hashMap))
            .execute(callback)
    }

    /**
     * 获取设备清单
     */
    fun <T> getList(callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("sessionid", AccountData.getSession())
        return OkGo.post<T>(DeviceApi.GET_LIST.url).params("", MapConvert.jsonConvert(hashMap))
            .execute(callback)
    }

    /**
     * 获取设备详情
     */
    fun <T> getDetail(deviceId:Int,callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("sessionid", AccountData.getSession())
        hashMap.put("deviceid", "${deviceId}")
        return OkGo.post<T>(DeviceApi.GET_DETAIL.url).params("", MapConvert.jsonConvert(hashMap))
            .execute(callback)
    }
}