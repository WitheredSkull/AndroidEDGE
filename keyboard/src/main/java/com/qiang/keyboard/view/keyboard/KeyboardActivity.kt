package com.qiang.keyboard.view.keyboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.qiang.keyboard.R

class KeyboardActivity : BaseKeyboardActivity() {
    override fun appendText(text: String) {
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
