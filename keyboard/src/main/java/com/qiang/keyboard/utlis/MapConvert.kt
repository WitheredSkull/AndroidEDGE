package com.qiang.keyboard.utlis

import android.util.Log
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.safety.EdgeBase64Utils
import org.json.JSONObject

object MapConvert {
    fun jsonConvert(map: HashMap<String, String>): String {
        val json = JSONObject(map)
        val string = EdgeBase64Utils.encrypt(json.toString())
        EdgeLog.show(javaClass,"请求参数",string.replace("\n",""))
        return string
    }
}