package com.qiang.keyboard.viewModel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.qiang.keyboard.databinding.ActivityDeviceDetailBinding
import com.qiang.keyboard.model.entity.DeviceItem
import com.qiang.keyboard.model.entity.DeviceListEntity
import com.qiang.keyboard.model.network.callBack.EntityCallBack
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.DeviceRequest
import com.qiang.keyboard.viewModel.base.BaseViewModel

class DeviceViewModel(application: Application) : BaseViewModel(application) {
    val mDevices = ObservableArrayList<DeviceItem>()
    var mDevice: DeviceItem? = null

    fun getList(srl: SwipeRefreshLayout) {
        DeviceRequest.getList(object : EntityCallBack<DeviceListEntity>(DeviceListEntity::class.java) {
            override fun onStart(request: Request<DeviceListEntity, out Request<Any, Request<*, *>>>?) {
                super.onStart(request)
                srl.isRefreshing = true
            }

            override fun success(code: Int, status: Int, des: String, body: Response<DeviceListEntity>) {
                mDevices.clear()
                mDevices.addAll(body.body().DataList)
            }

            override fun onFinish() {
                super.onFinish()
                srl.isRefreshing = false
            }
        })
    }

    fun alterNickName(dataBinding: ActivityDeviceDetailBinding) {
        val newValue = dataBinding.etNickname.text.toString()
        val imei = mDevice?.IMEI
        if (mDevice != null && !EdgeTextUtils.isEmpty(newValue, imei))
            DeviceRequest.setNickName(newValue, imei!!, object : String64CallBack() {
                override fun success(code: Int, status: Int, des: String, body: Response<String>) {
                    dataBinding.etNickname.setText(newValue)
                    mDevice?.Nickname = newValue
                    EdgeToastUtils.getInstance().show(des)
                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    dataBinding.etNickname.setText(mDevice?.Nickname)
                }
            })
    }

    fun getDetail(dataBinding: ActivityDeviceDetailBinding, deviceId: Int) {
        DeviceRequest.getDetail(deviceId, object : EntityCallBack<DeviceItem>(DeviceItem::class.java) {
            override fun success(code: Int, status: Int, des: String, body: Response<DeviceItem>) {
                this@DeviceViewModel.mDevice = body.body()
                dataBinding.tvId.text = "${body.body().DeviceId}"
                dataBinding.etNickname.setText(body.body().Nickname)
                dataBinding.tvStatus.text = if (body.body().isActive == 0) {
                    "离线"
                } else {
                    "在线"
                }
                dataBinding.tvType.text = "${body.body().TypeId}"
            }
        })
    }
}