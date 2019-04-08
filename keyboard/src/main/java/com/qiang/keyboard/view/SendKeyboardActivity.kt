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

class SendKeyboardActivity : BaseKeyboardActivity() {
    override fun changeText(text: String) {
        tv_message.text = text
    }

    override fun initAction(): String {
        return getString(R.string.action_forthwith)
    }

    lateinit var mClNumber: ConstraintLayout
    lateinit var tv_message: TextView

    override fun onHideNumber(isHide: Boolean) {
        if (isHide) {
            mClNumber.visibility = View.GONE
        } else {
            mClNumber.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        mClNumber = findViewById(R.id.cl_number)
        title = "当前模式： 即时发送"
        tv_message = findViewById(R.id.tv_message)
    }
}
