package com.qiang.keyboard.model.entity

import com.daniel.edge.utils.text.EdgeTextUtils

data class RegisterEntity(
    var MemberId: Int,
    var Username: String,
    var Email: String,
    var Nickname: String,
    var Avatar: String,
    var Mobile: String,
    var Sex: Int,
    var Description: String,
    var Birthday: String,
    var Address: String,
    var SessionId: String,
    var Status: Int,
    var IsShop: Int
) {
    fun getSex() = if (Sex == 0) {
        "男"
    } else {
        "女"
    }
}