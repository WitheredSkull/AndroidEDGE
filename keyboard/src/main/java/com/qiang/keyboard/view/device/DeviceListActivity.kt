package com.qiang.keyboard.view.device

import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ActivityDeviceListBinding
import com.qiang.keyboard.model.adapter.DeviceAdapter
import com.qiang.keyboard.view.base.BaseVMActivity
import com.qiang.keyboard.viewModel.DeviceViewModel

class DeviceListActivity : BaseVMActivity<ActivityDeviceListBinding, DeviceViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list, true)
    }

    override fun initListener() {
    }

    override fun initData() {
        enableMenu()
        getDataBinding().rvDevice.layoutManager = LinearLayoutManager(this)
        getDataBinding().rvDevice.adapter = DeviceAdapter(getViewModel()!!.mDevices)
        getDataBinding().srlDevice.setOnRefreshListener {
            getViewModel()?.getList(getDataBinding().srlDevice)
        }
    }

    override fun onResume() {
        super.onResume()
        getViewModel()?.getList(getDataBinding().srlDevice)
    }
}
