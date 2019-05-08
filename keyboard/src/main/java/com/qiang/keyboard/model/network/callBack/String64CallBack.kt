package com.qiang.keyboard.model.network.callBack

import android.text.TextUtils
import com.daniel.edge.utils.safety.EdgeBase64Utils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.model.network.callBack.ICallBack
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.convert.StringConvert
import com.lzy.okgo.model.Response
import org.json.JSONObject

abstract class String64CallBack: AbsCallback<String>, ICallBack<String> {
    private var convert: StringConvert

    var code = -1
    var status = -1
    var description = ""
    var encodeData = ""
    var data = ""

    constructor() : super() {
        convert = StringConvert()
    }

    override fun convertResponse(response: okhttp3.Response): String? {
        var s = convert.convertResponse(response)
        response.close()
        var json = JSONObject(s)
        code = json.getInt("Code")
        status = json.getInt("Status")
        description = json.getString("Description")
        if (status == 1) {
            encodeData = json.getString("Data")
            data = EdgeBase64Utils.decode(encodeData)
            if (TextUtils.isEmpty(data)) {
                return null
            } else{
                return data
            }
        } else {
            return null
        }
    }

    override fun onSuccess(response: Response<String>) {
        if (status == 1 && response.body() != null) {
            onEncodeData(encodeData)
            success(code, status, description, response)
        } else {
            error(code, status, description)
        }
    }

    open fun onEncodeData(s:String){

    }

    open fun error(code: Int, status: Int, des: String) {
        EdgeToastUtils.getInstance().show(des)
    }
    // 在 kotlin中扩展 Java 类的 Gson.fromJson 方法
// 不在传入 T 的class，inline 的作用就是将函数插入到被调用处，配合 reified 就可以获取到 T 真正的类型
//    inline fun <reified E : Any> Gson.fromJson(json: String): E {
//        return Gson().fromJson(json, E::class.java)
//    }

    inline fun <reified E> genericType() = object : TypeToken<E>() {}.type
}