package com.daniel.edge.utils.netWork

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.daniel.edge.config.Edge

/**
 * 网络支持的一些小工具
 */
object EdgeNetWorkStatusUtils {
    /**
     * @return 网络是否连接
     */
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = Edge.CONTEXT
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isConnected
        }
        return false
    }

    /**
     * @return WIFI是否连接
     */
    fun isWifiConnected(): Boolean {
        val mConnectivityManager = Edge.CONTEXT
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWiFiNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isConnected
        }

        return false
    }

    /**
     * Wifi是否打开
     */
    fun wifiEnable():Boolean{
        val _w = Edge.CONTEXT.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!_w.isWifiEnabled){
            _w.setWifiEnabled(true)
        }
        return _w.isWifiEnabled
    }
}