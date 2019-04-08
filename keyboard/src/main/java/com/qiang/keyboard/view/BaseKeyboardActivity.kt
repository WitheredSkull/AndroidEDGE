package com.qiang.keyboard.view

import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.qiang.keyboard.expand.createOptionsMenu
import com.qiang.keyboard.expand.optionsItemSelected
import com.qiang.keyboard.presenter.KeyboardInterface
import com.qiang.keyboard.service.KeyboardReceiver

abstract class BaseKeyboardActivity : AppCompatActivity(), KeyboardInterface, Observer<String> {

    //View的Cursor TAG
    var mAction = ""
    //是否需要返回
    var mIsNeedBack = true
    //按键反馈服务
    var mKeyboardReceiver: KeyboardReceiver? = null
    var mMessageLiveData = MutableLiveData<String>().apply {
        value = "请开始输入:"
    }
    var mMessage: StringBuilder = StringBuilder()

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
            changeText(it)
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

    override fun onSendText(text: String) {

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
        super.onDestroy()
    }

    abstract fun changeText(text: String)
    abstract fun initAction(): String

}