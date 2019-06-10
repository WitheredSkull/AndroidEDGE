package com.qiang.keyboard.model.api

import com.qiang.keyboard.utlis.Base32

// CMD_USER_LOGIN {"GUID":"2011-3211-2351-112","UserName":"Test","Password":"123456"} // 使用账户名和密码登录
// CMD_KEY_NEW {"GUID":"2011-3211-2351-312","From":1235,"To":2415,"Msg":"N"} // 发送N键给我的指定设备2415
// CMD_KEY_NEW {"GUID":"2011-3211-2351-442","From":1235,"To":0,"Msg":"N"} // 发送N键给所有我的设备
// CMD_HOST_NEW {"GUID":"2011-3211-2351-251","TypeId":1,"SessionId":"21234-32151-21352-12312","HostName":"水果手机01"} // 针对用户新注册一台手机设备
// CMD_HOST_REMOVE {"GUID":"2011-3211-2351-251","TypeId":1,"SessionId":"21234-32151-21352-12312"} // 针对用户新删除一台手机设备

object BaseApi {
    val KeyboardWebSocket = "ws://176.122.139.177:2000"

    //http协议的webSocket
//    val KeyboardWebSocket = "ws://176.122.139.177:2019"
    //https协议的webSocket
//    val KeyboardWebSocket = "wss://176.122.139.177:2020"
    val KeyboardSendTextFormat = "sessionid+[待发送的文本的base32编码]+待接收的设备id"
    var sessionid = ""

    fun getKeyboardFormatText(guid: String, from: String, to: String, content: String): String {
        return "${sessionid}+[${Base32.encode(content.toByteArray())}]+待接收的设备id"
    }

    fun getKeyboardFormatWord(content: String): String {
        val guid = 0
        val from = 0
        val to = 0
        return "CMD_KEY_NEW {\"GUID\":\"${guid}\",\"From\":${from},\"To\":${to},\"Msg\":\"${content}\"} // 发送N键给所有我的设备"
//        return "${sessionid}+{${Base32.encode(content.toByteArray())}}+待接收的设备id"
    }
}