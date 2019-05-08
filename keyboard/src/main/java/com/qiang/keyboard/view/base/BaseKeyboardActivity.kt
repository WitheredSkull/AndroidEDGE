package com.qiang.keyboard.view.base

import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.daniel.edge.management.bluetooth.EdgeBluetoothManager
import com.qiang.keyboard.expand.createOptionsMenu
import com.qiang.keyboard.expand.optionsItemSelected
import com.qiang.keyboard.presenter.KeyboardInterface
import com.qiang.keyboard.service.KeyboardReceiver

abstract class BaseKeyboardActivity : AppCompatActivity(), KeyboardInterface, Observer<String> {

    //View的Cursor TAG
    var mAction = ""
    var mIsNeedBack = true
    //按键反馈服务
    var mKeyboardReceiver: KeyboardReceiver? = null
    var mMessage: StringBuilder = StringBuilder()
    var mMessageLiveData = MutableLiveData<String>().apply {
        value = "请开始输入:"
    }

    override fun onChart(text: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAction = initAction()
        setTitle(mAction)
        mKeyboardReceiver = KeyboardReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(KeyboardReceiver.KeyboardAction)
        registerReceiver(mKeyboardReceiver, intentFilter)
        mMessageLiveData.observe(this, this)
    }

    override fun onChanged(s: String?) {
        s?.let {
            appendText(mMessage.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return createOptionsMenu(menu, mIsNeedBack)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (optionsItemSelected(item))
            return true
        else
            super.onOptionsItemSelected(item)
    }

    override fun onDelete() {
        if (mMessage.length > 0) {
            mMessage.deleteCharAt(mMessage.length - 1)
        } else {
            mMessage.clear()
        }
        mMessageLiveData.value = mMessage.toString()
    }

    override fun onEnter() {
        mMessage.append("\n")
        mMessageLiveData.value = mMessage.toString()
    }

    override fun onHideNumber(isHide: Boolean) {
    }

    override fun onInput(text: String) {
        EdgeBluetoothManager.getInstance().sendMessage(text)
        mMessage.append(text)
        mMessageLiveData.value = mMessage.toString()
    }

    override fun onSpace() {
        mMessage.append(" ")
        mMessageLiveData.value = mMessage.toString()
    }

    override fun onBack() {
        if (mMessage.length > 0) {
            mMessage.deleteCharAt(mMessage.length - 1)
        } else {
            mMessage.clear()
        }
        mMessageLiveData.value = mMessage.toString()
    }

    override fun onDestroy() {
        unregisterReceiver(mKeyboardReceiver)
        mMessageLiveData.removeObserver(this)
        mMessage.clear()
        super.onDestroy()
    }

    abstract fun appendText(text: String)
    abstract fun initAction(): String

}