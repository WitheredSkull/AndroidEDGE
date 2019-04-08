package com.qiang.keyboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.presenter.KeyboardController
import com.qiang.keyboard.presenter.KeyboardInterface
import com.qiang.keyboard.utlis.AudioUtils
import com.qiang.keyboard.utlis.VibrateUtlis

class KeyboardReceiver : BroadcastReceiver {

    var keyboardInterface: KeyboardInterface? = null
    override fun onReceive(context: Context, intent: Intent) {
        //先震动一手
        VibrateUtlis.vibrator()
        //确认按键的功能
        if (intent != null && intent.action.equals(KeyboardAction)) {
            var text = intent.getStringExtra(Text)
            when (intent.getIntExtra(Function, 0)) {
                KeyboardReceiverFunction.Input.ordinal -> {
                    text?.let {
                        keyboardInterface?.onInput(it)
                    }
                }
                KeyboardReceiverFunction.Enter.ordinal -> {
                    keyboardInterface?.onEnter()
                }
                KeyboardReceiverFunction.Delete.ordinal -> {
                    keyboardInterface?.onDelete()
                }
                KeyboardReceiverFunction.Space.ordinal -> {
                    keyboardInterface?.onSpace()
                }
                KeyboardReceiverFunction.Number.ordinal -> {
                    keyboardInterface?.onHideNumber(!KeyboardController.getInstance(intent.getStringExtra(Tag)).isNumber)
                }
                KeyboardReceiverFunction.Cap.ordinal -> EdgeLog.show(javaClass, "功能", "大写模式")
                KeyboardReceiverFunction.SendText.ordinal -> keyboardInterface?.onSendText(
                    intent.getStringExtra(
                        SendText
                    )
                )
                else -> EdgeLog.show(javaClass, "功能", "启动了其他功能")
            }
            //然后回首播放一个声音
            text?.let {
                keyboardInterface?.onInput(it)
            }
        }
    }


    constructor(keyboardInterface: KeyboardInterface) : super() {
        this.keyboardInterface = keyboardInterface
    }

    constructor() : super()

    companion object {
        const val Function = "Function"
        const val IsLongClick = "IsLongClick"
        const val KeyboardAction = "Keyboard"
        const val Tag = "Tag"
        const val Text = "Text"
        const val SendText = "SendText"
    }
}

enum class KeyboardReceiverFunction {
    SendText,
    Input,
    Delete,
    Enter,
    Space,
    Number,
    Cap,
    Shift
}
