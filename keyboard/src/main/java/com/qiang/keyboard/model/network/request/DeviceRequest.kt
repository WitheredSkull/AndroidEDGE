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
    fun <T> setNickName(nickName: String, callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("sessionid", AccountData.getSession())
        hashMap.put("imei", EdgeSystemUtils.getMacAddress())
        hashMap.put("nickname", nickName)
        return OkGo.post<T>(DeviceApi.SET_NIKE_NAME.url).params("", MapConvert.jsonConvert(hashMap))
            .execute(callback)
    }

    /**
     * 获取设备清单
     */
    fun <T> setList(callback: Callback<T>) {
        val hashMap = hashMapOf<String, String>()
        hashMap.put("sessionid", AccountData.getSession())
        return OkGo.post<T>(DeviceApi.SET_LIST.url).params("", MapConvert.jsonConvert(hashMap))
            .execute(callback)
    }
}