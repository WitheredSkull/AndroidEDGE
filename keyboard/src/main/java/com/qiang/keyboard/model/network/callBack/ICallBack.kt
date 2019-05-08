package com.qiang.keyboard.model.network.callBack

import com.lzy.okgo.model.Response

interface  ICallBack<T> {
    fun success(code:Int,status:Int,des:String,body: Response<T>)
}