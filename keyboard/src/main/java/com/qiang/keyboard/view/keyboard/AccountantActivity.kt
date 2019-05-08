package com.qiang.keyboard.view.keyboard

import android.os.Bundle
import com.qiang.keyboard.R
import com.qiang.keyboard.view.base.BaseKeyboardActivity
import kotlinx.android.synthetic.main.activity_send_accountant.*

class AccountantActivity : BaseKeyboardActivity() {
    override fun initAction(): String {
        return getString(R.string.action_accountant)
    }

    override fun appendText(text: String) {
        tv_text.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_accountant)
    }
}
