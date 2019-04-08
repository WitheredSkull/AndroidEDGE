package com.qiang.keyboard.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.daniel.edge.utils.viewUtils.EdgeViewHelperUtils
import com.qiang.keyboard.R
import com.qiang.keyboard.expand.createOptionsMenu
import com.qiang.keyboard.expand.optionsItemSelected
import com.qiang.keyboard.service.KeyboardReceiver
import kotlinx.android.synthetic.main.activity_send_text.*

class SendTextActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_send -> {
                //发送逻辑
                var intent = Intent()
                intent.action = KeyboardReceiver.KeyboardAction
                intent.putExtra(KeyboardReceiver.SendText, et_text.text.toString())
                sendBroadcast(intent)
                //发送之后需要删除
                et_text.setText("")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_text)
        EdgeViewHelperUtils.setOnClicks(this, bt_send)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return createOptionsMenu(menu, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (optionsItemSelected(item))
            return true
        else
            super.onOptionsItemSelected(item)
    }
}
