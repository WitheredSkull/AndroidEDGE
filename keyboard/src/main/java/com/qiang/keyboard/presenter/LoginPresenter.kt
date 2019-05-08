package com.qiang.keyboard.presenter

import androidx.appcompat.app.AppCompatActivity
import com.daniel.edge.utils.system.EdgeSystemUtils
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.lzy.okgo.model.Response
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.AccountRequest
import com.qiang.keyboard.view.account.LoginCallback

class LoginPresenter: BasePresenter<LoginCallback> {
    constructor(activity: AppCompatActivity) : super(activity)

    fun login(account:String,pwd:String){
//        if (EdgeTextUtils.isEmpty(account) ||
//            EdgeTextUtils.isEmpty(pwd)
//        ) {
//            EdgeToastUtils.getInstance().show("请完整输入账号密码")
//        } else {
//            val mac = EdgeSystemUtils.getMacAddress()
//            if (EdgeTextUtils.isEmpty(mac)){
//                EdgeToastUtils.getInstance().show("机器检测设备码失败，请尝试打开WIFI")
//                return
//            }
//            AccountRequest.login<String>(
//                account,
//                pwd,
//                mac,
//                AccountRequest.DeviceType.Send
//            ).execute(object : String64CallBack() {
//                override fun success(code: Int, status: Int, des: String, body: Response<String>) {
//                    getCallback()?.onLoginSuccess()
//                }
//
//                override fun error(code: Int, status: Int, des: String) {
//                    getCallback()?.onLoginFail(des)
//                }
//            })
//        }
    }
}