package com.qiang.keyboard.utlis

import android.preference.PreferenceManager
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.sharePreference.EdgeSharePreferencesUtils
import com.qiang.keyboard.constant.SPConfig

object InputUtils {
    fun isOpen(): Boolean {
        return EdgeSharePreferencesUtils.getSPProperty(SPConfig.FILE, SPConfig.INPUT, true)
    }

    fun switch(flag: Boolean) {
        return EdgeSharePreferencesUtils.setSPProperty(SPConfig.FILE, SPConfig.INPUT, flag)
    }
}