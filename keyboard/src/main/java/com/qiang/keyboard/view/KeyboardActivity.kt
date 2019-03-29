package com.qiang.keyboard.view

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.qiang.keyboard.presenter.KeyboardInterface
import com.qiang.keyboard.service.KeyboardReceiver
import com.qiang.keyboard.R
import java.lang.StringBuilder

class KeyboardActivity : AppCompatActivity(), KeyboardInterface {


    lateinit var keyboardReceiver: KeyboardReceiver
    lateinit var mClNumber: ConstraintLayout
    var mMessage = StringBuilder()
    lateinit var tv_message: TextView

    override fun onHideNumber(isHide: Boolean) {
        if (isHide) {
            mClNumber.visibility = View.GONE
        } else {
            mClNumber.visibility = View.VISIBLE
        }
    }

    override fun onSpace() {
        tv_message.setText(mMessage.append(" "))
    }

    override fun onEnter() {
        tv_message.setText(mMessage.append("\n"))
    }

    override fun onDelete() {
        if (mMessage.length > 0) {
            tv_message.setText(mMessage.deleteCharAt(mMessage.length - 1))
        } else {
            tv_message.setText("")
        }
    }

    override fun onInput(text: String) {
        tv_message.setText(mMessage.append(text))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        mClNumber = findViewById(R.id.cl_number)
        title = "当前模式： 即时发送"
        tv_message = findViewById(R.id.tv_message)
        keyboardReceiver = KeyboardReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(KeyboardReceiver.KeyboardAction)
        registerReceiver(keyboardReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_keyboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_back -> {
                onBackPressed()
            }
            R.id.action_exit -> {
                EdgeActivityManagement.getInstance().exit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        unregisterReceiver(keyboardReceiver)
        super.onDestroy()
    }
}
