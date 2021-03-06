package com.qiang.keyboard.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.daniel.edge.utils.system.EdgeSystemUtils
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.lzy.okgo.model.Response
import com.qiang.keyboard.R
import com.qiang.keyboard.model.data.AccountData
import com.qiang.keyboard.model.entity.LoginEntity
import com.qiang.keyboard.model.entity.RegisterEntity
import com.qiang.keyboard.model.network.callBack.EntityCallBack
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.AccountRequest
import com.qiang.keyboard.view.SelectActivity
import com.qiang.keyboard.view.account.AccountActivity
import com.qiang.keyboard.view.account.ForgetFragment
import com.qiang.keyboard.view.account.LoginFragment
import com.qiang.keyboard.view.account.RegisterFragment
import com.qiang.keyboard.viewModel.base.BaseViewModel

class AccountViewModel(application: Application) : BaseViewModel(application) {
    var fragmentManager = MutableLiveData<FragmentManager>()
    val account = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val verify = MutableLiveData<String>()
    var countDownTimer: CountDownTimer? = null
    //当ForgetType为0时找回密码，为1时修改密码
    var forgetType = 0

    init {
        val model = AccountData.getAccountInfo()
        account.value = model[0]
        pwd.value = model[1]
    }

    fun setFragmentManager(fm: FragmentManager) {
        fragmentManager.value = fm
    }

    fun getVerify(_v: View) {
        _v as TextView
        if (EdgeTextUtils.isEmpty(account.value)) {
            EdgeToastUtils.getInstance().show("请完整输入账号")
            return
        }
        if (countDownTimer == null) {
            AccountRequest.getSMSVerify(
                account.value!!,
                object : String64CallBack() {
                    override fun success(code: Int, status: Int, des: String, body: Response<String>) {
                    }
                }
            )
            countDownTimer = object : CountDownTimer(60000, 1000) {
                override fun onFinish() {
                    _v.setText(R.string.get_verify)
                    countDownTimer = null
                }

                override fun onTick(millisUntilFinished: Long) {
                    _v.text = "${millisUntilFinished / 1000}秒后重试"
                }
            }.start()
        } else {

        }
    }

    fun login() {
        if (EdgeTextUtils.isEmpty(account.value) ||
            EdgeTextUtils.isEmpty(pwd.value)
        ) {
            EdgeToastUtils.getInstance().show("请完整输入账号密码")
            return
        }
        val mac = EdgeSystemUtils.getMacAddress()
        if (EdgeTextUtils.isEmpty(mac)) {
            EdgeToastUtils.getInstance().show("机器检测设备码失败，请尝试打开WIFI")
            return
        }
        AccountRequest.login(
            account.value!!,
            pwd.value!!,
            mac,
            AccountRequest.DeviceType.Send,
            object : EntityCallBack<LoginEntity>(LoginEntity::class.java) {
                override fun success(code: Int, status: Int, des: String, body: Response<LoginEntity>) {
                    saveUserInfo(body.body())
                    startActivity(Intent(getContext(), SelectActivity::class.java))
                    EdgeActivityManagement.getInstance().finishOnClass(AccountActivity::class.java)
                }

                override fun error(code: Int, status: Int, des: String) {
                    super.error(code, status, des)
                }
            }
        )
    }

    fun saveUserInfo(model: LoginEntity) {
        AccountData.setAccountInfo(account.value, pwd.value)
        AccountData.setMemberInfo(model.MemberResult)
        AccountData.setDeviceInfo(model.Device)
    }

    fun forget() {
        if (EdgeTextUtils.isEmpty(account.value) ||
            EdgeTextUtils.isEmpty(pwd.value) ||
            EdgeTextUtils.isEmpty(verify.value)
        ) {
            EdgeToastUtils.getInstance().show("请完整输入账号密码")
            return
        }
        val mac = EdgeSystemUtils.getMacAddress()
        if (EdgeTextUtils.isEmpty(mac)) {
            EdgeToastUtils.getInstance().show("机器检测设备码失败，请尝试打开WIFI")
            return
        }
        when (forgetType) {
            0 -> AccountRequest.forgetPWD(
                account.value!!,
                pwd.value!!,
                verify.value!!,
                object : EntityCallBack<RegisterEntity>(RegisterEntity::class.java) {
                    override fun success(code: Int, status: Int, des: String, body: Response<RegisterEntity>) {
                        EdgeToastUtils.getInstance().show(des)
                        AccountData.setAccountInfo(pwd=pwd.value)
                        fragmentManager.value?.popBackStack()
                    }
                }
            )
            1 -> AccountRequest.alterPWD(
                account.value!!,
                pwd.value!!,
                verify.value!!,
                object : String64CallBack() {
                    override fun success(code: Int, status: Int, des: String, body: Response<String>) {
                        EdgeToastUtils.getInstance().show(des)
                        AccountData.setAccountInfo(pwd=pwd.value)
                        EdgeActivityManagement.getInstance().findTopActivity()
                    }

                }
            )
        }

    }

    fun register() {
        if (EdgeTextUtils.isEmpty(account.value) ||
            EdgeTextUtils.isEmpty(pwd.value) ||
            EdgeTextUtils.isEmpty(verify.value)
        ) {
            EdgeToastUtils.getInstance().show("请完整输入账号密码")
            return
        }
        val mac = EdgeSystemUtils.getMacAddress()
        if (EdgeTextUtils.isEmpty(mac)) {
            EdgeToastUtils.getInstance().show("机器检测设备码失败，请尝试打开WIFI")
            return
        }
        AccountRequest.register(
            account.value!!,
            pwd.value!!,
            verify.value!!,
            object : EntityCallBack<RegisterEntity>(RegisterEntity::class.java) {
                override fun success(code: Int, status: Int, des: String, body: Response<RegisterEntity>) {
                    EdgeToastUtils.getInstance().show(des)
                    fragmentManager.value?.popBackStack()
                }
            }
        )
    }

    enum class AccountEnum {
        Login, Register, ForgerPWD
    }

    fun switchFragment(type: AccountEnum, forgetType: Int = 0) {
        fragmentManager.value?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.nav_default_enter_anim,
                R.anim.nav_default_exit_anim,
                R.anim.nav_default_pop_enter_anim,
                R.anim.nav_default_pop_exit_anim
            )
            ?.replace(
                R.id.frameLayout,
                when (type) {
                    AccountEnum.Login -> LoginFragment()
                    AccountEnum.Register -> RegisterFragment()
                    AccountEnum.ForgerPWD -> {
                        val fragment = ForgetFragment()
                        val args = Bundle()
                        args.putInt("type", forgetType)
                        fragment.arguments = args
                        fragment
                    }
                }
            )
            ?.addToBackStack("account")
            ?.commit()
    }

    fun closeCountDownTime() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
    }

    fun switchKeyboard() {
        (getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
    }
}