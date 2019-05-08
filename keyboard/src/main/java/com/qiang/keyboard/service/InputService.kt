package com.qiang.keyboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.daniel.edge.management.bluetooth.EdgeBluetoothManager
import com.qiang.keyboard.R

class InputService : InputMethodService(), CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.tb_web -> {
                isReceiverWeb = isChecked
            }
            R.id.tb_bluetooth -> {
                if (isChecked && EdgeBluetoothManager.getInstance().isSupport) {
                    if (!EdgeBluetoothManager.getInstance().bluetoothAdapter!!.isEnabled) {
                        EdgeBluetoothManager.getInstance().bluetoothAdapter!!.enable()
                        return
                    }
                }
                isReceiverBluetooth = isChecked
            }
        }
    }

    var isReceiverWeb = false
    var isReceiverBluetooth = false
    var windowIsHidden = true
    var mCmmitTextRecevier: CommitTextRecevier? = null
    lateinit var mTbWeb: ToggleButton
    lateinit var mTbBluetooth: ToggleButton

    lateinit var mView: View
    override fun onCreateInputView(): View {
        mView = layoutInflater.inflate(R.layout.layout_keyboard, null)
        mTbWeb = mView.findViewById(R.id.tb_web)
        mTbBluetooth = mView.findViewById(R.id.tb_bluetooth)
        mTbWeb.isChecked = isReceiverWeb
        mTbBluetooth.isChecked = isReceiverBluetooth
        mTbWeb.setOnCheckedChangeListener(this)
        mTbBluetooth.setOnCheckedChangeListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_WEB_COMMIT_TEXT)
        intentFilter.addAction(ACTION_Bluetooth_COMMIT_TEXT)
        mCmmitTextRecevier = CommitTextRecevier()
        registerReceiver(mCmmitTextRecevier, intentFilter)
        startService(Intent(this, WebSocketService::class.java))
        startService(Intent(this, BluetoothService::class.java))
        return mView
    }

    override fun onWindowHidden() {
        windowIsHidden = true
        super.onWindowHidden()
    }

    override fun onWindowShown() {
        windowIsHidden = false
        super.onWindowShown()
    }

    override fun onDestroy() {
        isReceiverWeb = false
        isReceiverBluetooth = false
        unregisterReceiver(mCmmitTextRecevier)
        stopService(Intent(this, WebSocketService::class.java))
        stopService(Intent(this, BluetoothService::class.java))
        super.onDestroy()
    }

    inner class CommitTextRecevier : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent == null || windowIsHidden) {
                return
            }
            when (intent.action) {
                ACTION_WEB_COMMIT_TEXT -> {
                    if (isReceiverWeb)
                    inputText(intent.getStringExtra(RECEIVER_DATA))
                }
                ACTION_Bluetooth_COMMIT_TEXT -> {
                    if (isReceiverBluetooth)
                    inputText(intent.getStringExtra(RECEIVER_DATA))
                }
            }
        }
    }

    fun inputText(text: String?) {
        text?.let {
            currentInputConnection.commitText(it, 1)
        }
    }

    companion object {
        val ACTION_WEB_COMMIT_TEXT = "WebCommitText"
        val ACTION_Bluetooth_COMMIT_TEXT = "BluetoothCommitText"
        val RECEIVER_DATA = "data"
    }
}
