package com.qiang.keyboard.presenter

import android.os.Build
import android.os.VibrationEffect
import com.qiang.keyboard.constant.App
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class KeyboardController {
    companion object {
        @Volatile
        private var KeyboardControllerMap = mutableMapOf<Any, KeyboardController>()

        @Synchronized
        fun getInstance(tag: Any): KeyboardController = if (KeyboardControllerMap.get(tag) == null) {
            val instance = KeyboardController()
            KeyboardControllerMap.put(tag, instance)
            instance
        } else {
            KeyboardControllerMap.get(tag)!!
        }

        @Synchronized
        fun getTagMap() = KeyboardControllerMap

    }


//    WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {//主要的几个方法(在子线程中回调,刷新UI记得使用Handler)
//        @Override
//        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
//            //连接成功
//        }
//
//        @Override
//        public void onMessage(WebSocket webSocket, String text) {
//            super.onMessage(webSocket, text);
//            //接收服务器消息 text
//        }
//
//        @Override
//        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            super.onMessage(webSocket, bytes);
//            //如果服务器传递的是byte类型的
//            String msg = bytes.utf8();
//        }
//
//        @Override
//        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
//            super.onFailure(webSocket, t, response);
//            //连接失败调用 异常信息t.getMessage()
//        }
//    });


    //是否是大写
    var isCapital = false
    //是否是小键盘
    var isNumber = false
    //是否是开启大小写变化
    var isShift = false
}