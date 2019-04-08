package com.qiang.keyboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qiang.keyboard.R
import kotlinx.android.synthetic.main.activity_send_accountant.*

class SendAccountantActivity : BaseKeyboardActivity() {
    override fun initAction(): String {
        return getString(R.string.action_accountant)
    }

    override fun changeText(text: String) {
        tv_text.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_accountant)
    }
}
