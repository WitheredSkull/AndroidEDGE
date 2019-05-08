package com.qiang.keyboard.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.daniel.edge.utils.viewUtils.EdgeViewHelper
import com.qiang.keyboard.R
import com.qiang.keyboard.view.base.BaseKeyboardActivity
import kotlinx.android.synthetic.main.activity_select_keyboard.*

class SelectKeyboardActivity : BaseKeyboardActivity(), View.OnClickListener {
    override fun appendText(text: String) {

    }

    override fun initAction(): String {
        return getString(R.string.action_input_keyboard)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_setting_input_keyboard -> {

                var intent = Intent()
                intent.action = Settings.ACTION_INPUT_METHOD_SETTINGS
                startActivity(intent)
            }
            R.id.tv_select -> (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_keyboard)
        val type = intent.getIntExtra(KEYBOARD_TYPE, 0)
        tv_title.setText(
            when (type) {
                0 ->
                    R.string.action_input_keyboard
                1 ->
                    R.string.open_bluetooth_keyboard
                else ->
                    R.string.action_input_keyboard
            }
        )
        EdgeViewHelper.setOnClicks(
            this, tv_setting_input_keyboard,
            tv_select
        )
    }

    companion object {
        val KEYBOARD_TYPE = "keyboardType"
    }
}
