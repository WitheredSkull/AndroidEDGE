package com.daniel.edge.model.entity

data class LoginModel(var data:Any,var errorCode:Int,var errorMsg:String) {

    override fun toString(): String {
        return "LoginModel(data='$data', errorCode=$errorCode, errorMsg='$errorMsg')"
    }
}