package com.qiang.keyboard.model.network.api

import com.qiang.keyboard.model.network.api.BaseApi.Base_Api


object AccountApi {
    val SMS_VERIFY = "${Base_Api}/Api/Login/sendsms"
    val Register = "${Base_Api}/Api/Login/Register"
    val Login = "${Base_Api}/Api/Login/login"
    val Forget = "${Base_Api}/Api/Login/ForgetPassword"
    val AlterPWD = "${Base_Api}/Api/Login/ChangePassword"
}