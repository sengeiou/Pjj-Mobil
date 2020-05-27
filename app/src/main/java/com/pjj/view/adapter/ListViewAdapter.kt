package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.widget.ListView

/**
 * Created by XinHeng on 2019/04/10.
 * describe：
 */
abstract class ListViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var list: MutableList<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addMore(addList: MutableList<T>?) {
        addList?.let {
            if (null != list) {
                val count = itemCount
                val changeCount = it.size
                list!!.addAll(it)
                notifyItemRangeChanged(count, changeCount)
            } else {
                list = it
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}