package com.qiang.keyboard.view.account

import com.qiang.keyboard.view.base.BaseCallback

interface LoginCallback:BaseCallback {
    fun onLoginSuccess()
    fun onLoginFail(error:String)
}