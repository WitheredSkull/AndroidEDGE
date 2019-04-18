package com.qiang.keyboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.ToggleButton
import com.qiang.keyboard.R
import com.qiang.keyboard.utlis.InputUtils

class InputService : InputMethodService() {
    var isOpenTextReceive = true
    var mCmmitTextRecevier: CommitTextRecevier? = null

    lateinit var mView: View
    override fun onCreateInputView(): View {
        isOpenTextReceive = InputUtils.isOpen()
        mView = layoutInflater.inflate(R.layout.layout_keyboard, null)
        var tb = mView.findViewById<ToggleButton>(R.id.toggleButton)
        tb.isChecked = isOpenTextReceive
        tb.setOnCheckedChangeListener { buttonView, isChecked ->
            isOpenTextReceive = isChecked
            InputUtils.switch(isChecked)
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_COMMIT_TEXT)
        mCmmitTextRecevier = CommitTextRecevier()
        registerReceiver(mCmmitTextRecevier, intentFilter)
        startService(Intent(this, WebSocketService::class.java))
//        var keyboardView = mView.findViewById<KeyboardView>(R.id.keyboardView)
//        var keyboard = Keyboard(baseContext,R.xml.keyboard)
//        keyboardView.keyboard = keyboard
//        keyboardView.isEnabled = true
//        keyboardView.isPreviewEnabled = false
//        keyboardView.setOnKeyboardActionListener(this)
        return mView
    }

    override fun onWindowHidden() {
        isOpenTextReceive = false
        super.onWindowHidden()
    }

    override fun onWindowShown() {
        isOpenTextReceive = true
        super.onWindowShown()
    }

    override fun onDestroy() {
        unregisterReceiver(mCmmitTextRecevier)
        stopService(Intent(this, WebSocketService::class.java))
        super.onDestroy()
    }

    inner class CommitTextRecevier : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isOpenTextReceive) {
                if (intent.action.equals(ACTION_COMMIT_TEXT)) {
                    val text = intent.getStringExtra(RECEIVER_DATA)
                    text?.let {
                        currentInputConnection.commitText(it, 1)
                    }
                }
            }
        }
    }

    companion object {
        val ACTION_COMMIT_TEXT = "CommitText"
        val RECEIVER_DATA = "data"
    }
}
