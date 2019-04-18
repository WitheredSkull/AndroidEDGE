package com.qiang.keyboard.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.nfc.Tag
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.text.TextUtils
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.IKeyboardAidlInterface
import com.qiang.keyboard.R
import com.qiang.keyboard.constant.App
import com.qiang.keyboard.model.api.BaseApi
import com.qiang.keyboard.presenter.KeyboardInterface
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.LinkedBlockingQueue

class WebSocketService : Service, KeyboardInterface {

    //用于循环的Handler
    var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            var content = msg?.data?.getString("msg")
            if (msg != null && !content.isNullOrEmpty()) {
                EdgeToastUtils.getInstance().show(content!!)
            }
        }
    }
    var mIsConnection = false
    //按键反馈服务
    var mKeyboardReceiver: KeyboardReceiver? = null
    var mLinkedQueue = LinkedBlockingQueue<String>(1024)
    val mRequest = Request.Builder().url(BaseApi.KeyboardWebSocket).build()
    var mRunnable = object : Runnable {
        override fun run() {
            mHandler.postDelayed(this, 100)
            if (!mIsConnection) {
                mIsConnection = true
                mWebSocket = App.okHttpClient.newWebSocket(mRequest, mWebSocketListener)
            }
        }
    }
    var mWebSocket: WebSocket? = null
    var mWebSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            EdgeLog.show(javaClass, "WebSocket", "链接成功${Thread.currentThread().name}")
            mIsConnection = true
            sendBroadcast()
            var msg = Message()
            msg.data.putString("msg", "服务器连接成功!")
            mHandler.sendMessage(msg)
            super.onOpen(webSocket, response)
            while (!mLinkedQueue.isEmpty()) {
                sendMessage(mLinkedQueue.take())
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            t.printStackTrace()
            EdgeLog.show(javaClass, "WebSocket", "链接失败${Thread.currentThread().name}")
            mIsConnection = false
            sendBroadcast()
            var msg = Message()
            msg.data.putString("msg", "服务器连接失败!")
            mHandler.sendMessage(msg)
            super.onFailure(webSocket, t, response)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            EdgeLog.show(javaClass, "WebSocket", "关闭之前${Thread.currentThread().name}")
            super.onClosing(webSocket, code, reason)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val intent = Intent()
            intent.action = InputService.ACTION_COMMIT_TEXT
            intent.putExtra(InputService.RECEIVER_DATA,text)
            sendBroadcast(intent)
            EdgeLog.show(javaClass, "WebSocket", "收到消息${Thread.currentThread().name} ${text}")
            super.onMessage(webSocket, text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            EdgeLog.show(javaClass, "WebSocket", "收到消息${Thread.currentThread().name} ${bytes.toString()}")
            super.onMessage(webSocket, bytes)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            EdgeLog.show(javaClass, "WebSocket", "关闭长连接${Thread.currentThread().name} ")
            mIsConnection = false
            sendBroadcast()
            super.onClosed(webSocket, code, reason)
        }
    }
    var stub = object : IKeyboardAidlInterface.Stub() {
        override fun sendData(text: String?) {
            sendMessage(text!!)
        }
    }

    override fun onChart(text: String) {
    }

    override fun onInput(text: String) {
        sendMessage(text)
    }

    override fun onBack() {
    }

    override fun onDelete() {
    }

    override fun onEnter() {
    }

    override fun onSpace() {
    }

    override fun onHideNumber(isHide: Boolean) {
    }

    override fun onCreate() {
        super.onCreate()
        mKeyboardReceiver = KeyboardReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(KeyboardReceiver.KeyboardAction)
        registerReceiver(mKeyboardReceiver, intentFilter)
        setWebSocket()
    }

    override fun onBind(intent: Intent): IBinder? {
        return stub
    }

    override fun onDestroy() {
//        mWebSocket?.close(3078, "销毁")
        mHandler.removeCallbacks(mRunnable)
        mWebSocket?.cancel()
        unregisterReceiver(mKeyboardReceiver)
        super.onDestroy()
    }

    fun sendBroadcast() {
        var intent = Intent()
        intent.action = WebSocketReceiver.Action
        intent.putExtra(WebSocketReceiver.Function, WebSocketReceiver.Key_IsConnection)
        intent.putExtra(WebSocketReceiver.Key_IsConnection, mIsConnection)
        sendBroadcast(intent)
    }

    fun sendMessage(text: String) {
        if (!mIsConnection) {
            mLinkedQueue.put(BaseApi.getKeyboardFormatWord(text))
        } else {
            mWebSocket?.send(BaseApi.getKeyboardFormatWord(text))
        }
    }

    //设置webSocket
    fun setWebSocket() {
        mRunnable.run()
    }

    constructor() : super()
}
