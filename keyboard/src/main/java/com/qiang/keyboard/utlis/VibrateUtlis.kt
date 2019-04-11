package com.qiang.keyboard.utlis

import android.os.Build
import android.os.VibrationEffect
import android.preference.PreferenceManager
import com.daniel.edge.config.Edge
import com.qiang.keyboard.constant.App
import com.qiang.keyboard.constant.SPConfig

object VibrateUtlis {

    fun vibrator() {
        if (isSupport() && PreferenceManager.getDefaultSharedPreferences(Edge.CONTEXT).getBoolean(
                SPConfig.VIBRATION,
                true
            )) {
            App.vibrator.cancel()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                App.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                App.vibrator.vibrate(100)
            }
        }
    }

    fun isSupport(): Boolean {
        return App.vibrator.hasVibrator()
    }
}