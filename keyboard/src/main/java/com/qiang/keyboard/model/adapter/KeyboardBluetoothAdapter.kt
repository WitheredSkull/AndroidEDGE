package com.qiang.keyboard.model.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qiang.keyboard.R
import kotlin.collections.ArrayList

class KeyboardBluetoothAdapter(var list: ArrayList<BluetoothDevice>, var onListener: OnBluetoothConnectListener) :
    RecyclerView.Adapter<KeyboardBluetoothAdapter.ViewHolder>() {
    var socketConnectPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bluetooth_device, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var uuid = ""
        if (list[position].uuids != null) {
            list[position].uuids.forEach {
                uuid += it.uuid
            }
        }
        holder.tv_name.text = list[position].name
        holder.tv_address.text = list[position].address
        holder.tv_match.text = if (list[position].bondState == BluetoothDevice.BOND_BONDED) {
            "已配对"
        } else if (list[position].bondState == BluetoothDevice.BOND_BONDING) {
            "正在配对"
        } else {
            "没有配对"
        }
        if (position == socketConnectPosition) {
            holder.cb_connect.isChecked = true
            holder.cb_connect.text = "已连接"
        } else {
            holder.cb_connect.isChecked = false
            holder.cb_connect.text = "未连接"
        }
        holder.cb_connect.setOnCheckedChangeListener { buttonView, isChecked ->
            if (position != socketConnectPosition) {
                if (isChecked) {
                    onListener.onPrepareConnect(list[position])
                } else {
                    onListener.onOffConnect(list[position])
                    holder.cb_connect.text = "未连接"
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView
        var tv_address: TextView
        var tv_match: TextView
        var cb_connect: Switch

        init {
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_address = itemView.findViewById(R.id.tv_address)
            tv_match = itemView.findViewById(R.id.tv_match)
            cb_connect = itemView.findViewById(R.id.cb_connect)
        }
    }
}