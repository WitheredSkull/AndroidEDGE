package com.qiang.keyboard.view

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edge.utils.viewUtils.EdgeViewHelper
import com.lzy.okgo.model.Response
import com.qiang.keyboard.IKeyboardAidlInterface
import com.qiang.keyboard.R
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.ProgrammRequest
import com.qiang.keyboard.service.OnWebSocketListener
import com.qiang.keyboard.service.WebSocketReceiver
import com.qiang.keyboard.service.WebSocketService
import com.qiang.keyboard.view.base.BaseKeyboardActivity
import com.qiang.keyboard.view.keyboard.AccountantActivity
import com.qiang.keyboard.view.keyboard.CalculateActivity
import com.qiang.keyboard.view.keyboard.HalfKeyboardActivity
import com.qiang.keyboard.view.keyboard.KeyboardActivity
import com.qiang.keyboard.view.text.TextActivity
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : BaseKeyboardActivity(), View.OnClickListener, OnWebSocketListener {
    override fun appendText(text: String) {

    }

    override fun initAction(): String {
        return getString(R.string.action_select)
    }

    var isServiceConnection = false
    var mIKeyboardAidlInterface: IKeyboardAidlInterface? = null
    var mWebSocketIntent: Intent? = null
    var mWebSocketReceiver: WebSocketReceiver? = null
    var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mIKeyboardAidlInterface = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mIKeyboardAidlInterface = IKeyboardAidlInterface.Stub.asInterface(service)
            mIKeyboardAidlInterface?.sendData("测试")
        }
    }

    override fun onOpen() {
        isServiceConnection = true
    }

    override fun onClose() {
        isServiceConnection = false
    }

    override fun onClick(v: View) {
        if (!isServiceConnection && v.id != R.id.tv_bluetooth) {
            EdgeToastUtils.getInstance().show("服务器未连接")
            return
        }
        when (v.id) {
            R.id.tv_open_keyboard -> startActivity(Intent(this, SelectKeyboardActivity::class.java))
            //文本键盘
            R.id.tv_send_text -> startActivity(Intent(this, TextActivity::class.java))
            //即时键盘
            R.id.tv_send_forthwith -> startActivity(Intent(this, KeyboardActivity::class.java))
            R.id.tv_send_half_left -> {
                //左半键盘
                val intent = Intent(this, HalfKeyboardActivity::class.java)
                intent.putExtra(HalfKeyboardActivity.ACTION_AWAY, 0)
                startActivity(intent)
            }
            //右半键盘
            R.id.tv_send_half_right -> {
                val intent = Intent(this, HalfKeyboardActivity::class.java)
                intent.putExtra(HalfKeyboardActivity.ACTION_AWAY, 1)
                startActivity(intent)
            }
            //会计键盘
            R.id.tv_send_accountant -> startActivity(Intent(this, AccountantActivity::class.java))
            //计算器
            R.id.tv_calculate -> startActivity(Intent(this, CalculateActivity::class.java))
            //蓝牙键盘
            R.id.tv_bluetooth -> startActivity(Intent(this, SelectBluetoothActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        EdgeViewHelper.setOnClicks(
            this,
            tv_open_keyboard,
            tv_send_text,
            tv_send_forthwith,
            tv_send_half_left,
            tv_send_half_right,
            tv_send_accountant,
            tv_calculate,
            tv_bluetooth
        )
        onClose()
        mWebSocketIntent = Intent(this, WebSocketService::class.java)
//        startService(mWebSocketIntent)
        //开启WebSocket广播和服务用于发送和接收文本
        mWebSocketReceiver = WebSocketReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(WebSocketReceiver.Action)
        registerReceiver(mWebSocketReceiver, intentFilter)
        //开启WebSocket服务
//        bindService(mWebSocketIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        //应用更新
//        ProgrammRequest.AppUpdate(object :String64CallBack(){
//            override fun success(code: Int, status: Int, des: String, body: Response<String>) {
//
//            }
//        })
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        unregisterReceiver(mWebSocketReceiver)
//        stopService(mWebSocketIntent)
        super.onDestroy()
    }
}
