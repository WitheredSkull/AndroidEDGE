package com.daniel.edge.management.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Message
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.daniel.edge.config.Edge
import com.daniel.edge.management.bluetooth.model.BluetoothWhat
import com.daniel.edge.management.bluetooth.model.OnBluetoothCallback
import com.daniel.edge.utils.log.EdgeLog
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * 使用蓝牙需要申请权限
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 */

@SuppressLint("MissingPermission")
class EdgeBluetoothManager {
    //是否初始化数据
    private var isInit = false
    //重连次数三次
    var mReConnectCount = 3
    var mTempReConnectCount = 3

    //获取对应的Adapter
    var bluetoothAdapter: BluetoothAdapter? = null
    //连接线程
    private var connectThread: SocketConnectThread? = null
    //蓝牙设备
    var deriveList = arrayListOf<BluetoothDevice>()
    //当前的连接设备
    private var device: BluetoothDevice? = null
    //是否连接
    var isConnect = false
    //开启关闭接收消息消息循环
    private var isReceiversSocketLive = false
    //是否支持蓝牙功能
    val isSupport
        get() = bluetoothAdapter != null
    //蓝牙广播
    private var mBluetoothReceiver: BluetoothReceiver? = null
    //蓝牙功能回调
    private var mCallbacks: ArrayList<OnBluetoothCallback> = arrayListOf()
    //蓝牙Socket通信
    private var mClientSocket: BluetoothSocket? = null

    //远程连接设备
    var remoteDevice: BluetoothDevice? = null
    // What: -1连接失败，0连接成功,1发送消息,2接收消息
    private var mHandler =
        object : Handler() {
            override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
                when (BluetoothWhat.values()[msg.what]) {
                    BluetoothWhat.ConnectFail -> mCallbacks.forEach {
                        it.onConnectState(false)
                    }
                    BluetoothWhat.ConnectSuccess -> {
                        mCallbacks.forEach {
                            it.onConnectState(true)
                        }
                    }
                    BluetoothWhat.RemoteDeviceConnect ->
                        mCallbacks.forEach {
                            remoteDevice = msg.obj as BluetoothDevice
                            it.onRemoteDeviceConnect(remoteDevice!!)
                        }
                    BluetoothWhat.ReceiverMessage -> {
                        val content = msg.obj
                        if (content != null) {
                            mCallbacks.forEach {
                                it.onReceiver(content as String)
                            }
                        }
                    }
                    BluetoothWhat.CloseReceiversSocket -> {
                        mCallbacks.forEach { it.onCloseReceiversSocket() }
                        if (msg.data.getBoolean("isNeedConnect", false)) {
                            openSocketReceiversMagThread()
                        }
                    }
                }
            }
        }
    var mServiceSocket: BluetoothSocket? = null
    //接收线程
    private var receiverThread: SocketReceiverThread? = null

    /**
     * 添加回调接口，页面销毁需要remove，否则会内存泄漏
     */
    fun addCallback(callBack: OnBluetoothCallback): EdgeBluetoothManager {
        if (!mCallbacks.contains(callBack)) {
            mCallbacks.add(callBack)
        }
        return this
    }

    /**
     * 取消搜索蓝牙
     */
    @SuppressLint("MissingPermission")
    fun cancelDiscovery(): EdgeBluetoothManager {
        //是否在搜索
        if (bluetoothAdapter!!.isDiscovering) {
            bluetoothAdapter?.cancelDiscovery()
        }
        return this
    }

    fun closeSocket(interrupt: Boolean = false) {
        try {
            if (mClientSocket != null && mClientSocket!!.isConnected) {
                if (mClientSocket?.inputStream != null) {
                    mClientSocket?.inputStream?.close()
                }
                if (mClientSocket?.outputStream != null) {
                    mClientSocket?.outputStream?.close()
                }
                mClientSocket?.close()
            }
            if (mServiceSocket != null && mServiceSocket!!.isConnected) {
                if (mServiceSocket?.inputStream != null) {
                    mServiceSocket?.inputStream?.close()
                }
                if (mServiceSocket?.outputStream != null) {
                    mServiceSocket?.outputStream?.close()
                }
                mServiceSocket?.close()
            }
            isReceiversSocketLive = false
            if (interrupt && receiverThread != null && receiverThread!!.isAlive) {
                receiverThread!!.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mClientSocket = null
            mServiceSocket = null
            receiverThread = null
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun bindSocket(device: BluetoothDevice, reConnectCount: Int = 3): EdgeBluetoothManager {
        this.mReConnectCount = reConnectCount
        this.mTempReConnectCount = reConnectCount
        if (isSupport && bluetoothAdapter!!.isEnabled) {
            closeSocket()
            this.device = device
            if (connectThread == null || (connectThread != null && !connectThread!!.isAlive)) {
                connectThread = SocketConnectThread()
                connectThread?.start()
            }
        } else {
            EdgeLog.e(javaClass, "开启蓝牙Socket通信", "失败，设备不支持蓝牙或蓝牙未开启")
        }
        return this
    }

    /**
     * 销毁管理器
     */
    fun destroy() {
        remoteDevice = null
        isInit = false
        closeSocket()
        deriveList.clear()
        mCallbacks.clear()
        Edge.CONTEXT.unregisterReceiver(mBluetoothReceiver)
        mBluetoothReceiver = null
    }

    /**
     * 获取所有的设备
     */
    fun getDevices(): ArrayList<BluetoothDevice> {
        return deriveList
    }

    fun init(): EdgeBluetoothManager {
        if (!isInit) {
            isInit = true
            findBondedDevices()
            if (isSupport && mBluetoothReceiver == null) {
                registerBluetooth()
            }
        }
        return this
    }

    fun isReceiversSocketLive() = isReceiversSocketLive
    /**
     * 开启设备可被搜索
     * @param discoverableMillisecond 可被搜索时长
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun openDeviceDiscoverable(activity: AppCompatActivity, discoverableMillisecond: Long) {
        if (bluetoothAdapter!!.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            var intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, discoverableMillisecond)
            activity.startActivity(intent)
        }
    }

    /**
     * 开启循环接收功能
     */
    fun openSocketReceiversMagThread(): Boolean {
        if (!isSupport || !bluetoothAdapter!!.isEnabled) {
            throw Exception("Bluetooth not enable\n蓝牙未开启")
            return false
        }
        if (mServiceSocket == null || (mServiceSocket != null && !mServiceSocket!!.isConnected)) {
            closeSocket()
        }
        if (receiverThread == null || !(receiverThread != null && receiverThread!!.isAlive)) {
            receiverThread = SocketReceiverThread()
            receiverThread?.start()
            return true
        } else {
            return false
        }
    }

    /**
     * 删除监听,防止内存泄漏
     */
    fun removeCallback(callBack: OnBluetoothCallback) {
        if (mCallbacks.contains(callBack)) {
            mCallbacks.remove(callBack)
        }
    }

    /**
     * 搜索新的蓝牙
     * 前提是需要先调用registerBluetooth
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun searchRim(): EdgeBluetoothManager {
        cancelDiscovery()
        bluetoothAdapter?.startDiscovery()
        return this
    }

    fun sendMessage(msg: String): Boolean {
        var isSuccess = false
        if (mClientSocket == null && mServiceSocket == null) {
            EdgeLog.e(javaClass, "Bluetooth send message|蓝牙发送消息", "current time not device be connect\n当前没有设备被连接")
        }
        try {
            if (mClientSocket != null) {
                isSuccess = true
                mClientSocket?.outputStream?.write(msg.toByteArray())
                mClientSocket?.outputStream?.flush()
            }
            if (mServiceSocket != null) {
                isSuccess = true
                mServiceSocket?.outputStream?.write(msg.toByteArray())
                mServiceSocket?.outputStream?.flush()
            }
        } finally {
            return isSuccess
        }
    }

    fun findBondedDevices() {
        bluetoothAdapter?.bondedDevices?.forEach {
            if (!deriveList.contains(it)) {
                deriveList.add(it)
            }
        }
    }

    /**
     * 注册蓝牙的基本功能,如果使用自定义广播需要super以支持此框架
     */
    private fun registerBluetooth(receiver: BluetoothReceiver? = null): EdgeBluetoothManager {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        if (receiver != null) {
            mBluetoothReceiver = receiver
        } else {
            mBluetoothReceiver = BluetoothReceiver()
        }
        Edge.CONTEXT.registerReceiver(mBluetoothReceiver, intentFilter)
        return this
    }

    inner class SocketReceiverThread : Thread() {
        @RequiresPermission(Manifest.permission.BLUETOOTH)
        override fun run() {
            isReceiversSocketLive = true
            try {
                var inputStream: InputStream? = null
                if (mServiceSocket != null && mServiceSocket!!.isConnected) {
                    inputStream = mServiceSocket?.inputStream
                } else {
                    val localSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                        "btspp",
                        SERIAL_PORT_UUID
                    )
                    mClientSocket = localSocket?.accept()
                    inputStream = mClientSocket?.inputStream
                    //蓝牙Socket通信连接成功，返回主动连接的远程设备
                    val msg = Message()
                    msg.what = BluetoothWhat.RemoteDeviceConnect.ordinal
                    msg.obj = mClientSocket?.remoteDevice
                    mHandler.sendMessage(msg)
                }
                mTempReConnectCount = mReConnectCount
                while (isReceiversSocketLive) {
                    //字节流转String字符串
                    val buffer = ByteArray(1024)
                    val bytes = inputStream!!.read(buffer)
                    if (bytes > 0) {
                        val dataBytes = ByteArray(bytes)
                        for (i in 0..bytes - 1) {
                            dataBytes[i] = buffer[i]
                        }
                        val content = String(dataBytes)
                        val msg = Message()
                        msg.what = BluetoothWhat.ReceiverMessage.ordinal
                        msg.obj = content
                        mHandler.sendMessage(msg)
                        if (remoteDevice == null) {
                            //蓝牙Socket通信连接成功，返回主动连接的远程设备
                            val msgDevice = Message()
                            msgDevice.what = BluetoothWhat.RemoteDeviceConnect.ordinal
                            msgDevice.obj = mClientSocket?.remoteDevice
                            mHandler.sendMessage(msgDevice)
                        }
                    }
                }
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                remoteDevice = null
                val msg = Message()
                msg.what = BluetoothWhat.CloseReceiversSocket.ordinal
                if (isReceiversSocketLive && --mTempReConnectCount != 0) {
                    //如果不是正常断开则开始重连
                    msg.data.putBoolean("isNeedConnect", true)
                } else {
                    //如果是正常断开则开始清理资源
                    mClientSocket?.close()
                    mServiceSocket?.close()
                    mClientSocket = null
                    mServiceSocket = null
                }
                mHandler.sendMessage(msg)
            }
        }
    }

    /**
     * 连接线程，主动匹配并与附近的设备连接
     */
    inner class SocketConnectThread : Thread() {

        @RequiresPermission(Manifest.permission.BLUETOOTH)
        override fun run() {
            val connectMsg = Message()
            try {
                mServiceSocket = device?.createRfcommSocketToServiceRecord(SERIAL_PORT_UUID)
                mServiceSocket?.connect()
                remoteDevice = device
                isConnect = true
                connectMsg.what = BluetoothWhat.ConnectSuccess.ordinal
                mHandler.sendMessage(connectMsg)
                //连接成功后需要重新请求循环接收线程并且使用相对应的Socket连接
                openSocketReceiversMagThread()
            } catch (e: Exception) {
                isConnect = false
                connectMsg.what = BluetoothWhat.ConnectFail.ordinal
                mHandler.sendMessage(connectMsg)
                e.printStackTrace()
            }
        }
    }

    inner class BluetoothReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    //找到设备
                    val device = getDevices(intent)
                    if (!deriveList.contains(device)) {
                        deriveList.add(device)
                    }
                    mCallbacks.forEach {
                        it.onDiscoverDevices(deriveList)
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    //搜索完成
                    mCallbacks.forEach {
                        it.onCloseSearchDevices()
                    }
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            mCallbacks.forEach {
                                it.onCloseBluetooth()
                            }
                        }
                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            //正在关闭时需要先关闭Socket防止各种异常
                            closeSocket()
                        }
                        BluetoothAdapter.STATE_ON -> {
                            mCallbacks.forEach {
                                it.onOpenBluetooth()
                            }
                        }
                        BluetoothAdapter.STATE_TURNING_ON -> {
                        }
                    }
                }
            }
        }
    }

    init {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    object Instance {
        val INSTANCE = EdgeBluetoothManager()
    }

    companion object {

        //请求码
        val REQUEST_BLUETOOTH = 3001
        //串口UUID
        val SERIAL_PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        /**
         * 获取ActivityResult返回的新蓝牙设备
         */
        fun getDevices(intent: Intent): BluetoothDevice =
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        fun getInstance() = Instance.INSTANCE.init()
    }
}