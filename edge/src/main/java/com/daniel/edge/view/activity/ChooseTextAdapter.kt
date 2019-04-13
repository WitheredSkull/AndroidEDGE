package com.daniel.edge.view.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.edge.R

class ChooseTextAdapter : RecyclerView.Adapter<ChooseTextAdapter.ViewHolder> {
    var mList: Array<String>
    var mOnClick: OnAdapterClickListener? = null

    constructor(list: Array<String>, mOnClick: OnAdapterClickListener?) : super() {
        this.mList = list
        this.mOnClick = mOnClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = mList[position]
        holder.tv.setOnClickListener {
            mOnClick?.onClick(it, position, mList[position])
        }
    }


    class ViewHolder : RecyclerView.ViewHolder {
        var tv: TextView

        constructor(itemView: View) : super(itemView) {
            tv = itemView.findViewById(R.id.tv)
        }
    }

    interface OnAdapterClickListener {
        fun onClick(view: View, position: Int, data: String)
    }
}