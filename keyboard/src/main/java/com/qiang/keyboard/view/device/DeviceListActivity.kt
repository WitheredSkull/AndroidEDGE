package com.qiang.keyboard.view.device

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ActivityDeviceListBinding
import com.qiang.keyboard.view.base.BaseActivity
import com.qiang.keyboard.viewModel.DeviceViewModel

class DeviceListActivity : BaseActivity<ActivityDeviceListBinding,DeviceViewModel>() {
    override fun initDataBinding(): ActivityDeviceListBinding? {
       return DataBindingUtil.setContentView(this,R.layout.activity_device_list)
    }

    override fun initViewModel(dataBinding: ActivityDeviceListBinding) {
        setViewModel(DeviceViewModel::class.java).apply {
            getDataBinding()?.viewModel = this
        }
    }

    override fun initData() {
    }
}
