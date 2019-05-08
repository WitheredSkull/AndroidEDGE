package com.qiang.keyboard.model.adapter

import android.bluetooth.BluetoothDevice

interface OnBluetoothConnectListener {
    fun onPrepareConnect(device: BluetoothDevice)
    fun onOffConnect(device: BluetoothDevice)
}