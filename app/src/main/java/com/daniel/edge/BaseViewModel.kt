package com.daniel.edge

import android.view.View
import com.daniel.edge.Utils.Log.EdgeLog

class BaseViewModel {
    fun onClick(v: View){
        EdgeLog.show(javaClass,"测试")
    }
}