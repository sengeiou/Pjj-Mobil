package com.pjj.view.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.TopPriceBean
import com.pjj.utils.ViewUtils
import kotlin.math.min

class FreeZhiDingAdapter : RecyclerView.Adapter<FreeZhiDingAdapter.FreeZhiDingHolder>() {
    private var selectPosition = -1
    var datas: MutableList<TopPriceBean.DataBean>? = null
        set(value) {
            field = value
            selectPosition = min(1, itemCount - 1)//默认选择第2个
            if (selectPosition >= 0) {
                val bean = datas!![selectPosition]
                onFreeZhiDingAdapterListener?.itemClick(bean.price * bean.discount, bean.topId)
            }
            notifyDataSetChanged()
        }
    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        if (position != selectPosition) {
            //刷新ui
            val old = selectPosition
            selectPosition = position
            notifyItemChanged(old)
            (it as TextView).let { view ->
                view.background = getSelectBg()
                view.setTextColor(ViewUtils.getColor(R.color.color_ff4c4c))
            }
        }
        val bean = datas!![position]
        onFreeZhiDingAdapterListener?.itemClick(bean.price * bean.discount, bean.topId)
    }
    var onFreeZhiDingAdapterListener: OnFreeZhiDingAdapterListener? = null

    interface OnFreeZhiDingAdapterListener {
        fun itemClick(price: Float, topId: String)
    }

    fun isSelect(): Boolean {
        return selectPosition > -1
    }

    private fun getSelectBg(): Drawable {
        return ViewUtils.getDrawable(R.mipmap.xuanzhong_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeZhiDingHolder {
        return FreeZhiDingHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_free_zhiding_item, parent, false)).apply {
            tv_num.setOnClickListener(onClick)
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: FreeZhiDingHolder, position: Int) {
        val bean = datas!![position]
        holder.tv_num.text = bean.topName
        holder.tv_num.setTag(R.id.position, position)
        val discount = getDiscount(bean.discount)
        if (discount.isEmpty()) {
            holder.tv_discount.visibility = View.GONE
        } else {
            holder.tv_discount.visibility = View.VISIBLE
            holder.tv_discount.text = discount
        }
        holder.tv_num.background = if (position == selectPosition) getSelectBg() else ViewUtils.getDrawable(R.drawable.shape_999999_side_3)
        holder.tv_num.setTextColor(if (position == selectPosition) ViewUtils.getColor(R.color.color_ff4c4c) else ViewUtils.getColor(R.color.color_555555))
    }

    private fun getDiscount(discount: Float): String {
        var fl = getString(discount * 100)
        return if (fl == "100") "" else fl + "折"
    }

    private fun getString(value: Float): String {
        val intValue = value.toInt()
        return if (value - intValue > 0) {
            value.toString()
        } else {
            intValue.toString()
        }
    }

    class FreeZhiDingHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_num = itemView.findViewById<TextView>(R.id.tv_num)
        var tv_discount = itemView.findViewById<TextView>(R.id.tv_discount)
    }
}