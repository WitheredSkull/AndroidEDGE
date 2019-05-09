package com.qiang.keyboard.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.qiang.keyboard.BR

/**
 * @author Daniel
 * @time 2019/5/9
 * @param ITEM 数据的类型
 * @param ITEM_DATA_BINDING 每一个数据Item的DataBinding
 * @param list 需要传入ObservableArrayList
 * 使用时需要固定设置layout的variable name为"item"
 */
abstract class BaseAdapter<ITEM, ITEM_DATA_BINDING : ViewDataBinding>(var list: ObservableArrayList<ITEM>) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder<ITEM, ITEM_DATA_BINDING>>() {
    //用于更新数据的Observable监听器
    var onDataChangedListener: OnDataChangedListener? = null
    //是否自动更新数据
    var autoNotifyEnable = false

    init {
        enableAutoNotify()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ITEM, ITEM_DATA_BINDING>, position: Int) {
//        bindViewModel(holder.mBinding, list[position])
        holder.bind(list[position])
        setData(holder.mBinding, list[position], position)
        setListener(holder.mBinding, list[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ITEM, ITEM_DATA_BINDING> {
        val view = LayoutInflater.from(parent.context).inflate(getViewFromItem(), parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * 开启自动通知更新数据
     */
    fun enableAutoNotify(): BaseAdapter<ITEM, ITEM_DATA_BINDING> {
        if (onDataChangedListener == null) {
            onDataChangedListener = OnDataChangedListener()
        }
        if (!autoNotifyEnable) {
            list.addOnListChangedCallback(onDataChangedListener)
        }
        autoNotifyEnable = true
        return this
    }

    /**
     * 关闭自动通知更新数据
     */
    fun disableAutoNotyfy(): BaseAdapter<ITEM, ITEM_DATA_BINDING> {
        if (onDataChangedListener != null && autoNotifyEnable) {
            list.removeOnListChangedCallback(onDataChangedListener)
        }
        autoNotifyEnable = false
        return this
    }

    /**
     *
     */

    /**
     * 根据Item返回Layout
     */
    abstract fun getViewFromItem(): Int

    /**
     * 设置数据
     */
    abstract fun setData(dataBinding: ITEM_DATA_BINDING?, data: ITEM, position: Int)

    /**
     * 批量设置监听
     */
    abstract fun setListener(dataBinding: ITEM_DATA_BINDING?, data: ITEM, position: Int)

    class BaseViewHolder<ITEM, Binding : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBinding: Binding?

        init {
            mBinding = DataBindingUtil.bind(itemView)
        }

        fun bind(data: ITEM) {
            mBinding?.setVariable(BR.item, data)
            mBinding?.executePendingBindings()
        }
    }

    inner class OnDataChangedListener : ObservableList.OnListChangedCallback<ObservableArrayList<ITEM>>() {
        override fun onChanged(sender: ObservableArrayList<ITEM>?) {
            this@BaseAdapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<ITEM>?, positionStart: Int, itemCount: Int) {
            this@BaseAdapter.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(
            sender: ObservableArrayList<ITEM>?,
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) {
            this@BaseAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<ITEM>?, positionStart: Int, itemCount: Int) {
            this@BaseAdapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<ITEM>?, positionStart: Int, itemCount: Int) {
            this@BaseAdapter.notifyItemRangeChanged(positionStart, itemCount)
        }

    }
}
