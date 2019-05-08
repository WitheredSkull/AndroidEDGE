package com.qiang.keyboard.view

import android.animation.Animator
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiang.keyboard.model.adapter.KeyboardBluetoothAdapter
import com.qiang.keyboard.model.adapter.OnBluetoothConnectListener
import kotlinx.android.synthetic.main.activity_bluetooth.*
import com.daniel.edge.management.bluetooth.EdgeBluetoothManager
import com.daniel.edge.management.bluetooth.model.OnBluetoothCallback
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.daniel.edge.utils.viewUtils.EdgeViewHelper
import com.qiang.keyboard.R
import com.qiang.keyboard.service.BluetoothService
import com.qiang.keyboard.view.base.BaseKeyboardActivity


class BluetoothActivity : BaseKeyboardActivity(), View.OnClickListener, OnBluetoothConnectListener, OnBluetoothCallback {
    override fun initAction(): String {
        return getString(R.string.action_bluetooth)
    }

    override fun appendText(text: String) {
    }

    override fun onOpenBluetooth() {
        EdgeBluetoothManager.getInstance().findBondedDevices()
        mKeyboardBluetoothAdapter.notifyDataSetChanged()
    }

    override fun onCloseBluetooth() {
        mKeyboardBluetoothAdapter?.socketConnectPosition = -1
        mKeyboardBluetoothAdapter?.notifyDataSetChanged()
    }

    override fun onRemoteDeviceConnect(device: BluetoothDevice) {
        if (EdgeBluetoothManager.getInstance().deriveList.contains(device)) {
            var position = EdgeBluetoothManager.getInstance().deriveList.indexOf(device)
            mKeyboardBluetoothAdapter?.socketConnectPosition = position
            mKeyboardBluetoothAdapter?.notifyDataSetChanged()
        }
    }

    override fun onConnectState(isSuccess: Boolean) {
        if (isSuccess && connectDevice != null) {
            mKeyboardBluetoothAdapter?.socketConnectPosition =
                EdgeBluetoothManager.getInstance().deriveList.indexOf(connectDevice!!)
        } else {
            connectDevice = null
            mKeyboardBluetoothAdapter?.socketConnectPosition = -1
        }
        mKeyboardBluetoothAdapter?.notifyDataSetChanged()
    }

    override fun onReceiver(msg: String) {
        EdgeToastUtils.getInstance().show("${msg}")
    }

    override fun onCloseReceiversSocket() {
        mKeyboardBluetoothAdapter?.socketConnectPosition = -1
        mKeyboardBluetoothAdapter?.notifyDataSetChanged()
    }

    override fun onDiscoverDevices(devices: ArrayList<BluetoothDevice>) {
        mKeyboardBluetoothAdapter?.notifyDataSetChanged()
    }

    override fun onCloseSearchDevices() {
        pb.visibility = View.GONE
        pb.clearAnimation()
    }

    override fun onPrepareConnect(device: BluetoothDevice) {
        connectDevice = device
        mBluetoothBinder?.cancelSearch()
        mBluetoothBinder?.connectionDevice(device)
    }

    override fun onOffConnect(device: BluetoothDevice) {
        EdgeBluetoothManager.getInstance().closeSocket()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_refresh -> refresh()
            R.id.tv_send -> EdgeBluetoothManager.getInstance().sendMessage(et_text.text.toString())
        }
    }

    lateinit var mKeyboardBluetoothAdapter: KeyboardBluetoothAdapter
    var mServiceConnection: ServiceConnection? = null
    var mBluetoothBinder: BluetoothService.BluetoothBinder? = null
    var connectDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        if (EdgeBluetoothManager.getInstance().isSupport) {
            EdgeBluetoothManager.getInstance().addCallback(this)
            mServiceConnection = object : ServiceConnection {
                override fun onServiceDisconnected(name: ComponentName?) {

                }

                override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                    mBluetoothBinder = service as BluetoothService.BluetoothBinder
                }

            }
            val bluetoothService = Intent(this, BluetoothService::class.java)
            bindService(bluetoothService, mServiceConnection, Context.BIND_AUTO_CREATE)
        } else {
            finish()
        }

        pb.visibility = View.GONE
        mKeyboardBluetoothAdapter = KeyboardBluetoothAdapter(EdgeBluetoothManager.getInstance().deriveList, this)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mKeyboardBluetoothAdapter

         if (EdgeBluetoothManager.getInstance().bluetoothAdapter!!.isEnabled) {
            s_state.isChecked = true
        } else {
            s_state.isChecked = false
        }
        s_state.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                EdgeBluetoothManager.getInstance().bluetoothAdapter!!.enable()
            } else {
                EdgeBluetoothManager.getInstance().bluetoothAdapter!!.disable()
            }
        }
        EdgeViewHelper.setOnClicks(this, tv_refresh, tv_send)
        EdgeBluetoothManager.getInstance().remoteDevice?.let {
            onRemoteDeviceConnect(it)
        }
    }

    override fun onDestroy() {
        EdgeBluetoothManager.getInstance().removeCallback(this)
        unbindService(mServiceConnection)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EdgeBluetoothManager.REQUEST_BLUETOOTH) {
            EdgeBluetoothManager.getInstance().searchRim()
        }
    }

    fun refresh() {
        pb.visibility = View.VISIBLE
        pb.animate()
            .setDuration(1000)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    pb.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        mBluetoothBinder?.searchRim()
    }
}
