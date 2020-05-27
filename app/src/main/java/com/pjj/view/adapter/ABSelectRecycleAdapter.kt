package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import com.pjj.utils.TextUtils

/**
 * Created by XinHeng on 2018/12/13.
 * describe：
 */
abstract class ABSelectRecycleAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected open var listAdapter: MutableList<SelectAdapterBean<T>>? = null
    fun setList(list: MutableList<T>?) {
        if (TextUtils.isNotEmptyList(list)) {
            var listAdapter = ArrayList<SelectAdapterBean<T>>(list!!.size)
            list!!.forEachIndexed { index, it ->
                listAdapter.add(SelectAdapterBean<T>().apply {
                    bean = it
                    isEffective = isEffective(it, index)
                })
            }
            this.listAdapter = listAdapter
        } else {
            listAdapter = null
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return (listAdapter?.size ?: 0) + 0
    }

    fun getItemBean(position: Int): T? {
        return listAdapter!![position].bean
    }

    open fun isEffective(t: T, index: Int): Boolean {
        return true
    }

    class SelectAdapterBean<T> {
        var bean: T? = null
        var isSelect = false
            set(value) {
                field = if (isEffective) {
                    value
                } else {
                    false
                }
            }
        var isEffective = true
            set(value) {
                if (!value) {
                    isSelect = false
                }
                field = value
            }
    }
}