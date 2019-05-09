package com.qiang.keyboard.model.entity

data class LoginEntity(
    val Device: Device,
    val MemberResult: MemberResult
)

data class Device(
    val DeviceId: Int,
    val IMEI: String,
    val MemberId: Int,
    val Nickname: String,
    val SiteId: Int,
    val Status: Int,
    val TypeId: Int,
    val isActive: Int
)

data class MemberResult(
    val Address: String,
    val Avatar: String,
    val Birthday: String,
    val Description: String,
    val Email: String,
    val IsShop: Int,
    val MemberId: Int,
    val Mobile: String,
    val Nickname: String,
    val SessionId: String,
    val Sex: Int,
    val Status: Int,
    val Username: String
)