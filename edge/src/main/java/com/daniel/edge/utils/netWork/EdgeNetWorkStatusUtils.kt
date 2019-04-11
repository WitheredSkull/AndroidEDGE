package com.daniel.edge.utils.netWork

import android.content.Context
import android.net.ConnectivityManager
import com.daniel.edge.config.Edge


object EdgeNetWorkStatusUtils {
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = Edge.CONTEXT
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isConnected
        }
        return false
    }

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
}