package com.qiang.keyboard.view.launch

import android.content.Intent
import androidx.databinding.DataBindingUtil
import com.daniel.edge.utils.system.EdgeSystemUtils
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ActivityLaunchBinding
import com.qiang.keyboard.model.data.AccountData
import com.qiang.keyboard.view.SelectActivity
import com.qiang.keyboard.view.account.AccountActivity
import com.qiang.keyboard.view.base.BaseActivity
import com.qiang.keyboard.viewModel.LaunchViewModel
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.coroutines.*

class LaunchActivity : BaseActivity<ActivityLaunchBinding, LaunchViewModel>() {
    override fun initDataBinding(): ActivityLaunchBinding? {
        return DataBindingUtil.setContentView(this, R.layout.activity_launch)
    }

    override fun initViewModel(dataBinding: ActivityLaunchBinding) {
        setViewModel(LaunchViewModel::class.java).apply {
            getDataBinding()?.viewModel = this
        }
    }

    override fun initData() {
        //启动时间三秒
        val job = GlobalScope.launch(Dispatchers.Unconfined) {
            val isLogin = AccountData.isLogin()
            repeat(3) {
                delay(1000)
            }
            launch(Dispatchers.Main) {
                //如果登录直接跳转选择页，如果没有登录则跳转登录页
                if (isActive)
                    jumpLogin(isLogin)
            }
        }
        //点击快速进入
        if (EdgeSystemUtils.isApkInDebug()) {
            iv_icon.setOnClickListener {
                job.cancel()
                jumpLogin(true)
            }
        }
    }

    fun jumpLogin(isLogin: Boolean) {
        if (isLogin)
            startActivity(Intent(this@LaunchActivity, SelectActivity::class.java))
        else
            startActivity(Intent(this@LaunchActivity, AccountActivity::class.java))
    }
}
