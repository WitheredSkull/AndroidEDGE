package com.daniel.edge.management.bluetooth.model

import android.bluetooth.BluetoothDevice

interface OnBluetoothCallback {
    fun onOpenBluetooth()
    fun onConnectState(isSuccess:Boolean)
    fun onRemoteDeviceConnect(device: BluetoothDevice)
    fun onDiscoverDevices(devices:ArrayList<BluetoothDevice>)
    fun onReceiver(msg:String)
    fun onCloseReceiversSocket()
    fun onCloseSearchDevices()
    fun onCloseBluetooth()
}