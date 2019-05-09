package com.qiang.keyboard.service

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.daniel.edge.management.bluetooth.EdgeBluetoothManager
import com.daniel.edge.management.bluetooth.model.OnBluetoothCallback
import com.daniel.edge.utils.log.EdgeLog

class BluetoothService : Service(), OnBluetoothCallback {
    override fun onOpenBluetooth() {
        EdgeBluetoothManager.getInstance().openSocketReceiversMagThread()
    }

    override fun onCloseBluetooth() {
    }

    override fun onRemoteDeviceConnect(device: BluetoothDevice) {
    }
    override fun onDiscoverDevices(devices: ArrayList<BluetoothDevice>) {
    }

    override fun onCloseSearchDevices() {
    }

    override fun onConnectState(isSuccess: Boolean) {
    }

    override fun onReceiver(msg: String) {
        val intent = Intent()
        intent.action = InputService.ACTION_Bluetooth_COMMIT_TEXT
        intent.putExtra(InputService.RECEIVER_DATA,msg)
        sendBroadcast(intent)
    }
    override fun onCloseReceiversSocket() {
    }

    override fun onBind(intent: Intent): IBinder? {
        return BluetoothBinder()
    }

    override fun onCreate() {
        super.onCreate()
        EdgeBluetoothManager.getInstance()
            .addCallback(this)
        if (EdgeBluetoothManager.getInstance().bluetoothAdapter!!.isEnabled){
            EdgeBluetoothManager.getInstance().openSocketReceiversMagThread()
        }else{

        }
    }

    inner class BluetoothBinder: Binder() {
        fun cancelSearch(){
            EdgeBluetoothManager.getInstance().cancelDiscovery()
        }

        fun searchRim(){
            EdgeBluetoothManager.getInstance().searchRim()
        }

        fun connectionDevice(device: BluetoothDevice) {
            var connectDevice = EdgeBluetoothManager.getInstance().bluetoothAdapter!!.getRemoteDevice(device.address)
            EdgeBluetoothManager.getInstance().bindSocket(connectDevice)
        }
    }

    override fun onDestroy() {
//        EdgeBluetoothManager.getInstance().destroy()
        super.onDestroy()
    }
}
