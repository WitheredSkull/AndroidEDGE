package com.qiang.keyboard.view.device

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ActivityDeviceDetailBinding
import com.qiang.keyboard.view.base.BaseVMActivity
import com.qiang.keyboard.viewModel.DeviceViewModel

class DeviceDetailActivity : BaseVMActivity<ActivityDeviceDetailBinding, DeviceViewModel>() {
    override fun initData() {
        enableMenu()
        getViewModel()?.getDetail(getDataBinding(),intent.getIntExtra("deviceId",0))
    }

    override fun initListener() {
        getDataBinding().tvAlter.setOnClickListener {
            getViewModel()?.alterNickName(getDataBinding())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_detail, true)
    }
}
