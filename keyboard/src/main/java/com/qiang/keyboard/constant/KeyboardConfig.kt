package com.qiang.keyboard.constant

import com.daniel.edge.config.Edge
import com.qiang.keyboard.R

class KeyboardConfig {
    companion object {
        private var mInstance: KeyboardConfig? = null
            get() {
                return field ?: KeyboardConfig()
            }

        @Synchronized
        fun getInstance() = requireNotNull(mInstance)
    }

    val KeyboardFillter = arrayOf(Edge.CONTEXT.getString(R.string.action_calculate))
}