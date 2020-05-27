package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.IndustryBean

/**
 * Created by XinHeng on 2018/12/11.
 * describe：
 */
class IndustryAdapter : RecyclerView.Adapter<IndustryAdapter.IndustryHolder>() {
    var list: MutableList<IndustryBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryHolder {
        return IndustryHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_industry_item, parent, false).apply {
            setOnClickListener(onClick)
        })
    }

    private var onClick = View.OnClickListener {
        var position = it.tag as Int
        onItemClickListener?.itemClick(list!![position])
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: IndustryHolder, position: Int) {
        holder.itemView.tag = position
        var dataBean = list!![position]
        holder.tv_title.text = dataBean.dicName
        holder.tv_describe.text = dataBean.describe
    }

    class IndustryHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_title = view.findViewById<TextView>(R.id.tv_title)!!
        var tv_describe = view.findViewById<TextView>(R.id.tv_describe)!!
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun itemClick(dataBean: IndustryBean.DataBean)
    }
}