package com.qiang.keyboard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.daniel.edge.utils.viewUtils.EdgeViewHelper
import com.qiang.keyboard.R
import com.qiang.keyboard.view.base.BaseKeyboardActivity
import kotlinx.android.synthetic.main.activity_bluetooth_select.*

class SelectBluetoothActivity : BaseKeyboardActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_bluetooth_client -> {
                var intent = Intent(this, SelectKeyboardActivity::class.java)
                intent.putExtra(SelectKeyboardActivity.KEYBOARD_TYPE, 1)
                startActivity(intent)
            }
            R.id.tv_bluetooth_service -> {
                startActivity(Intent(this, BluetoothActivity::class.java))
            }
        }
    }

    override fun appendText(text: String) {

    }

    override fun initAction(): String {
        return getString(R.string.action_bluetooth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_select)
        EdgeViewHelper.setOnClicks(this, tv_bluetooth_client, tv_bluetooth_service)
    }
}
