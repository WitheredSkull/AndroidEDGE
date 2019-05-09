package com.qiang.keyboard.model.adapter

import android.content.Intent
import androidx.databinding.ObservableArrayList
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.log.EdgeLog
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ItemDeviceBinding
import com.qiang.keyboard.model.entity.DeviceItem
import com.qiang.keyboard.view.device.DeviceDetailActivity

class DeviceAdapter(list: ObservableArrayList<DeviceItem>) : BaseAdapter<DeviceItem, ItemDeviceBinding>(list) {

    override fun setListener(dataBinding: ItemDeviceBinding?, data: DeviceItem, position: Int) {
        dataBinding?.root?.setOnClickListener {
            val intent = Intent(Edge.CONTEXT,DeviceDetailActivity::class.java)
            intent.putExtra("deviceId",data.DeviceId)
            Edge.CONTEXT.startActivity(intent)
        }
    }
    override fun setData(dataBinding: ItemDeviceBinding?, data: DeviceItem, position: Int) {

    }

    override fun getViewFromItem(): Int {
        return R.layout.item_device
    }
}