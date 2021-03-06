package com.qiang.keyboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WebSocketReceiver : BroadcastReceiver {
    var onWebSocketListener: OnWebSocketListener? = null


    constructor(onWebSocketListener: OnWebSocketListener) : super() {
        this.onWebSocketListener = onWebSocketListener
    }

    constructor() : super()


    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action.equals(Action)) {
            when (intent.getStringExtra(Function)) {
                Key_IsConnection -> {
                    if (intent.getBooleanExtra(Key_IsConnection, false)) {
                        onWebSocketListener?.onOpen()
                    } else {
                        onWebSocketListener?.onClose()
                    }
                }
            }
        }
    }


    companion object {
        val Action = "WebSocketReceiver"
        val Function = "Function"
        val Key_IsConnection = "Key_IsConnection"
    }
}
