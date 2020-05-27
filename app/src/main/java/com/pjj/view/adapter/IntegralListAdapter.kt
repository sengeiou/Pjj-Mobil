package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.IntegralBean
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import java.util.*

/**
 * Created by XinHeng on 2019/04/10.
 * describe：
 */
class IntegralListAdapter : ListViewAdapter<IntegralBean.IntegralData, IntegralListAdapter.IntegralHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegralHolder {
        return IntegralHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_integral_list_item, parent, false))
    }

    private var date = Date()
    private val jiaColor = ViewUtils.getColor(R.color.color_04be46)
    private val jianColor = ViewUtils.getColor(R.color.color_ea4a4a)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IntegralHolder, position: Int) {
        list!![position].run {
            holder.tv_time.text = DateUtils.getSm(date.apply { time = createTime })
            var text = when (type) {
                "1" -> {//+
                    holder.tv_intefral.text = "+$integralNum"
                    holder.tv_intefral.setTextColor(jiaColor)
                    "广告投放"
                }
                "2" -> {
                    holder.tv_intefral.setTextColor(jianColor)
                    holder.tv_intefral.text = "-$integralNum"
                    "金币兑换"
                }
                else -> {
                    holder.tv_intefral.setTextColor(jianColor)
                    holder.tv_intefral.text = "-$integralNum"
                    "订单取消"
                }
            }
            if (!TextUtils.isEmpty(describe)) {
                text = "$text（$describe）"
            }
            holder.tv_text.text = text
        }
    }

    class IntegralHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_text = view.findViewById<TextView>(R.id.tv_text)
        var tv_time = view.findViewById<TextView>(R.id.tv_time)
        var tv_intefral = view.findViewById<TextView>(R.id.tv_integral)
    }
}