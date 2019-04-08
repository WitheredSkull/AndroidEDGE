package com.qiang.keyboard.model.api

import com.qiang.keyboard.utlis.Base32

object BaseApi {
    val KeyboardWebSocket = "ws://176.122.139.177:2000"
    val KeyboardSendTextFormat = "sessionid+[待发送的文本的base32编码]+待接收的设备id"
    var sessionid = ""

    fun getKeyboardFormatText(content: String): String {
        return "${sessionid}+[${Base32.encode(content.toByteArray())}]+待接收的设备id"
    }

    fun getKeyboardFormatWord(content: String): String {
        return "${sessionid}+{${Base32.encode(content.toByteArray())}}+待接收的设备id"
    }
}