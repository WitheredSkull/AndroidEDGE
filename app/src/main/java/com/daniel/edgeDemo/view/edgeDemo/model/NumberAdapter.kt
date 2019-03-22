package com.daniel.edgeDemo.view.edgeDemo.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.edgeDemo.R

class NumberAdapter : RecyclerView.Adapter<NumberAdapter.ViewHolder> {
    var context: Context
    var list: List<String>

    constructor(context: Context, list: List<String>) : super() {
        this.context = context
        this.list = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_number, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position]
    }


    inner class ViewHolder : RecyclerView.ViewHolder {
        var textView: TextView

        constructor(itemView: View) : super(itemView) {
            textView = itemView.findViewById(R.id.tv_number)
        }
    }
}