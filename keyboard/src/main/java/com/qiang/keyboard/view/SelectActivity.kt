package com.qiang.keyboard.view

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edge.utils.viewUtils.EdgeViewHelperUtils
import com.qiang.keyboard.IKeyboardAidlInterface
import com.qiang.keyboard.R
import com.qiang.keyboard.service.OnWebSocketConnectListener
import com.qiang.keyboard.service.WebSocketReceiver
import com.qiang.keyboard.service.WebSocketService
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity(), View.OnClickListener, OnWebSocketConnectListener {
    var isServiceConnection = false
    override fun onOpen() {
        for (index in 0..ll_select.childCount - 1) {
            ll_select.getChildAt(index).setBackgroundResource(R.drawable.shape_round_border_gray_100dp)
        }
        isServiceConnection = true
    }

    override fun onClose() {
        for (index in 0..ll_select.childCount - 1) {
            ll_select.getChildAt(index).setBackgroundResource(R.drawable.shape_round_gray_100dp)
        }
        isServiceConnection = false
    }

    var mIKeyboardAidlInterface: IKeyboardAidlInterface? = null
    var mWebSocketReceiver: WebSocketReceiver? = null
    var mWebSocketIntent: Intent? = null
    var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mIKeyboardAidlInterface = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mIKeyboardAidlInterface = IKeyboardAidlInterface.Stub.asInterface(service)
            mIKeyboardAidlInterface?.sendData("测试")
        }

    }

    override fun onClick(v: View) {
        if (!isServiceConnection) {
            EdgeToastUtils.getInstance().show("服务器未连接")
            return
        }
        when (v.id) {
            //文本键盘
            R.id.tv_send_text -> startActivity(Intent(this, SendTextActivity::class.java))
            //即时键盘
            R.id.tv_send_forthwith -> startActivity(Intent(this, SendKeyboardActivity::class.java))
            //左半键盘

            R.id.tv_send_half_left -> {
                var intent = Intent(this, SendHalfKeyboardActivity::class.java)
                intent.putExtra(SendHalfKeyboardActivity.ACTION_AWAY, 0)
                startActivity(intent)
            }
            //右半键盘
            R.id.tv_send_half_right -> {
                var intent = Intent(this, SendHalfKeyboardActivity::class.java)
                intent.putExtra(SendHalfKeyboardActivity.ACTION_AWAY, 1)
                startActivity(intent)
            }
            //会计键盘
            R.id.tv_send_accountant -> startActivity(Intent(this, SendAccountantActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        EdgeViewHelperUtils.setOnClicks(
            this,
            tv_send_text,
            tv_send_forthwith,
            tv_send_half_left,
            tv_send_half_right,
            tv_send_accountant
        )
        onClose()
        mWebSocketIntent = Intent(this, WebSocketService::class.java)
//        startService(mWebSocketIntent)

        mWebSocketReceiver = WebSocketReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(WebSocketReceiver.Action)
        registerReceiver(mWebSocketReceiver, intentFilter)
        bindService(mWebSocketIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        unregisterReceiver(mWebSocketReceiver)
//        stopService(mWebSocketIntent)
        super.onDestroy()
    }
}
