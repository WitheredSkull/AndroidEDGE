package com.qiang.keyboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.presenter.KeyboardController
import com.qiang.keyboard.presenter.KeyboardInterface

class KeyboardReceiver : BroadcastReceiver {

    var keyboardInterface: KeyboardInterface? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null && intent.action.equals(KeyboardAction)) {
            when (intent.getIntExtra(Function, 0)) {
                KeyboardReceiverFunction.Input.ordinal -> {
                    keyboardInterface?.onInput(intent.getStringExtra(Text))
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
                else -> EdgeLog.show(javaClass, "功能", "启动了其他功能")
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
        const val Text = "Text"
        const val Tag = "Tag"
    }
}

enum class KeyboardReceiverFunction {
    Input,
    Delete,
    Enter,
    Space,
    Number,
    Cap,
    Shift
}
