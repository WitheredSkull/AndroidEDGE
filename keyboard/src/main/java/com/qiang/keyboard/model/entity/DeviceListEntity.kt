package com.qiang.keyboard.model.entity

data class DeviceListEntity(
    val DataList: List<DeviceItem>,
    val Page: Int,
    val PageSize: Int,
    val TotalCount: Int
)

data class DeviceItem(
    val DeviceId: Int,
    val IMEI: String,
    val MemberId: Int,
    var Nickname: String,
    val SiteId: Int,
    val Status: Int,
    val TypeId: Int,
    val isActive: Int
)