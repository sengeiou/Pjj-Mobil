package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.ExchangeRecordBean
import com.pjj.module.IntegralBean
import com.pjj.utils.DateUtils
import com.pjj.utils.ViewUtils
import java.util.*

/**
 * Created by XinHeng on 2019/04/10.
 * describe：金币兑换
 */
class ExchangeRecordListAdapter : ListViewAdapter<ExchangeRecordBean.ExchangeRecordData, ExchangeRecordListAdapter.ExchangeRecordHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRecordHolder {
        return ExchangeRecordHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_goods_list_item, parent, false)).apply {
            itemView.setOnClickListener(onClick)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExchangeRecordHolder, position: Int) {
        holder.itemView.setTag(R.id.position, position)
        list!![position].run {
            Glide.with(holder.itemView).load(PjjApplication.integralFilePath + goodsPicture).into(holder.iv_goods)
            holder.tv_goods_statue.text = when (status) {
                "1" -> "待发货"
                "2" -> "待收货"
                else -> "已完成"
            }
            holder.tv_goods_name.text = goodsName
            //holder.tv_goods_num.text = "x1"
            holder.tv_integral.text = "${goodsIntegral}金币"
            holder.tv_freight.text = "运费：$postCost 元"
        }
    }

    class ExchangeRecordHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_goods = view.findViewById<ImageView>(R.id.iv_goods)
        var tv_goods_statue = view.findViewById<TextView>(R.id.tv_goods_statue)
        var tv_goods_name = view.findViewById<TextView>(R.id.tv_goods_name)
        var tv_integral = view.findViewById<TextView>(R.id.tv_integral)
        var tv_freight = view.findViewById<TextView>(R.id.tv_freight)
        var tv_goods_num = view.findViewById<TextView>(R.id.tv_goods_num)
    }

    private var onClick = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        var data = list!![position]
        onExchangeRecordAdapterListener?.itemClick(data)
    }
    var onExchangeRecordAdapterListener: OnExchangeRecordAdapterListener? = null

    interface OnExchangeRecordAdapterListener {
        fun itemClick(data: ExchangeRecordBean.ExchangeRecordData)
    }
}