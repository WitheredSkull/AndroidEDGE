package com.qiang.keyboard.model.network.api

import com.qiang.keyboard.model.network.api.BaseApi.Base_Api


enum class ProgrammApi(val url:String) {
    //获取设备更新
    APP_Update("${Base_Api}/Api/AppUpdata"),
}