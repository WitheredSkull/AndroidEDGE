package com.qiang.keyboard.model.network.api

import com.qiang.keyboard.model.network.api.BaseApi.Base_Api


enum class DeviceApi(val url:String) {
    //获取设备列表
    GET_LIST("${Base_Api}/Api/Device/GetList"),
    //获取设备详情
    GET_DETAIL("${Base_Api}/Api/Device/GetDetail"),
    //设置设备昵称
    SET_NIKE_NAME("${Base_Api}/Api/Device/SetDeviceNickname"),

}